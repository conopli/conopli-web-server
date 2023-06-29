package conopli.webserver.music.service;

import conopli.webserver.constant.ErrorCode;
import conopli.webserver.dto.HttpClientDto;
import conopli.webserver.dto.PageResponseDto;
import conopli.webserver.dto.ResponseDto;
import conopli.webserver.exception.ServiceLogicException;
import conopli.webserver.music.dto.UserMusicDto;
import conopli.webserver.music.dto.UserMusicRequestDto;
import conopli.webserver.music.entity.UserMusic;
import conopli.webserver.music.repository.UserMusicRepository;
import conopli.webserver.playlist.dto.PlayListDto;
import conopli.webserver.playlist.dto.PlayListModifyRequestDto;
import conopli.webserver.playlist.dto.PlayListRequestDto;
import conopli.webserver.playlist.entity.PlayList;
import conopli.webserver.playlist.repository.PlayListRepository;
import conopli.webserver.service.HttpClientService;
import conopli.webserver.user.entity.User;
import conopli.webserver.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserMusicService {

    private final PlayListRepository playListRepository;

    private final UserMusicRepository userMusicRepository;

    private final UserRepository userRepository;

    private final HttpClientService httpClientService;

    public ResponseDto findUserPlayList(Long userId) {
        User findUser = userRepository.findUserById(userId);
        List<PlayList> playListByUser = playListRepository.findPlayListByUser(findUser);
        List<PlayListDto> response = playListByUser.stream().map(PlayListDto::of).collect(Collectors.toList());
        return ResponseDto.of(response);
    }

    public PageResponseDto findUserMusic(Long playListId, Pageable pageable) {
        PlayList findPlayList = playListRepository.findPlayListById(playListId);
        Page<UserMusic> findList = userMusicRepository.findUserMusicByPlayList(findPlayList, pageable);
        List<UserMusicDto> responseList = findList.getContent().stream().map(UserMusicDto::of)
                .collect(Collectors.toList());
        return PageResponseDto.of(responseList, findList);
    }

    public ResponseDto createUserPlayList(PlayListRequestDto requestDto) {
        User findUser = userRepository.findUserById(requestDto.getUserId());
        PlayList newPlayList = PlayList.of(requestDto);
        findUser.addPlayList(newPlayList);
        PlayList saveList = playListRepository.savePlayList(newPlayList);
        return ResponseDto.of(PlayListDto.of(saveList));
    }

    public ResponseDto createUserMusic(UserMusicRequestDto requestDto) {
        PlayList findPlayList = playListRepository.findPlayListById(requestDto.getPlayListId());
        ArrayList<UserMusic> userMusicList = new ArrayList<>();
        for (String musicNum : requestDto.getMusicNum()) {
            HttpClientDto dto = httpClientService.generateSearchMusicByNumRequest(musicNum);
            UserMusic newUserMusic = UserMusic.of(dto);
            findPlayList.addUserMusic(newUserMusic);
            int countingOrder = findPlayList.getCountingOrder();
            newUserMusic.setOrderNum(countingOrder);
            findPlayList.setCountingOrder(countingOrder + 1);
            UserMusic saveUserMusic = userMusicRepository.saveUserMusic(newUserMusic);
            userMusicList.add(saveUserMusic);
        }
        return ResponseDto.of(userMusicList.stream().map(UserMusicDto::of).toList());
    }

    public void duplicationUserMusic(Long playListId) {
        PlayList findPlayList = playListRepository.findPlayListById(playListId);
        Set<UserMusic> userMusic = findPlayList.getUserMusic();
        List<String> userMusicNum =
                userMusic.stream()
                        .map(UserMusic::getNum)
                        .toList();
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String num : userMusicNum) {
            if (map.get(num) != null && map.get(num) >= 1) {
                Integer count = map.get(num);
                map.put(num, count+1);
            } else {
                map.putIfAbsent(num, 1);
            }
        }
        List<String> listNum = map.entrySet().stream()
                .filter(e -> e.getValue() >= 2)
                .map(Map.Entry::getKey)
                .toList();
        List<UserMusic> musicList = new ArrayList<>();
        List<UserMusic> emptyList = new ArrayList<>();
        List<UserMusic> deleteList = new ArrayList<>();
        for (String num : listNum) {
            userMusic.stream().filter(um -> um.getNum().equals(num))
                    .forEach(musicList::add);
        }
        for (UserMusic music : musicList) {
            boolean emptyBool = emptyList.stream().anyMatch(um -> um.getNum().equals(music.getNum()));
            if (!emptyBool) {
                emptyList.add(music);
            } else {
                deleteList.add(music);
            }
        }
        deleteList.forEach(um -> userMusicRepository.deleteUserMusicByUserMusicId(um.getMusicId()));
    }

    public ResponseDto modifyPlayList(Long playListId, PlayListRequestDto requestDto) {
        PlayList findPlayList = playListRepository.findPlayListById(playListId);
        PlayList updatePlayList = findPlayList.updatePlayList(requestDto);
        return ResponseDto.of(PlayListDto.of(updatePlayList));
    }

    public PageResponseDto modifyUserMusic(PlayListModifyRequestDto requestDto) {
        Long playListId = requestDto.getPlayListId();
        PlayList findPlayList = playListRepository.findPlayListById(playListId);
        Set<UserMusic> userMusic = findPlayList.getUserMusic();
        // 2,4,5,6,7,1,3
        List<Integer> orderList = requestDto.getOrderList();
        List<UserMusic> setList = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            Integer orderNum = orderList.get(i);
            UserMusic findUserMusic = userMusic.stream()
                    .filter(a -> a.getOrderNum() == orderNum)
                    .findFirst()
                    .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER_MUSIC));
            setList.add(findUserMusic);
        }
        for (int i = 0; i < setList.size(); i++) {
            UserMusic findUserMusic = setList.get(i);
            findUserMusic.setOrderNum(i);
        }
        Pageable pageable = PageRequest.of(0, 10, Sort.by("orderNum").ascending());
        Page<UserMusic> findList = userMusicRepository.findUserMusicByPlayList(findPlayList, pageable);
        List<UserMusicDto> responseList = findList.getContent().stream()
                .map(UserMusicDto::of)
                .collect(Collectors.toList());
        return PageResponseDto.of(responseList, findList);
    }

    public void deleteUserPlayList(Long playListId) {
        playListRepository.deletePlayListById(playListId);
    }

    public void deleteUserMusic(PlayListModifyRequestDto requestDto) {
        requestDto.getOrderList().forEach(id ->
                userMusicRepository.deleteUserMusicByUserMusicId(Long.valueOf(id))
        );
    }


}
