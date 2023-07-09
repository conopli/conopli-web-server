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
        PlayList playList = playListRepository.findPlayListById(playListId);
        Set<UserMusic> userMusic = playList.getUserMusic();
        List<UserMusic> duplicatedMusic = userMusic.stream()
                .collect(Collectors.groupingBy(UserMusic::getNum))
                .values()
                .stream()
                .filter(musicList -> musicList.size() > 1)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        List<UserMusic> deleteList = getDeleteUserMusicList(duplicatedMusic);
        deleteList.forEach(userMusic::remove);
    }

    private List<UserMusic> getDeleteUserMusicList(List<UserMusic> musicList) {
        // 중복되는 UserMusic 리스트 전체를 받아 첫번째 UserMusic을 제외한 나머지 UserMusic List를 반환한다
        List<UserMusic> deleteList = new ArrayList<>();
        Set<String> uniqueNums = new HashSet<>();
        musicList.forEach(music -> {
            if (!uniqueNums.add(music.getNum())) {
                deleteList.add(music);
            }
        });
        return deleteList;
    }

    public ResponseDto modifyPlayList(Long playListId, PlayListRequestDto requestDto) {
        PlayList findPlayList = playListRepository.findPlayListById(playListId);
        PlayList updatePlayList = findPlayList.updatePlayList(requestDto);
        return ResponseDto.of(PlayListDto.of(updatePlayList));
    }

    public PageResponseDto modifyUserMusic(PlayListModifyRequestDto requestDto) {
        Long playListId = requestDto.getPlayListId();
        PlayList playList = playListRepository.findPlayListById(playListId);
        playList.getUserMusic()
                .forEach(music ->
                        music.setOrderNum(requestDto.getOrderList().indexOf(music.getOrderNum()))
                );
        Pageable pageable = PageRequest.of(0, 10, Sort.by("orderNum").ascending());
        Page<UserMusic> findList = userMusicRepository.findUserMusicByPlayList(playList, pageable);
        List<UserMusicDto> responseList = findList.getContent()
                .stream()
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
