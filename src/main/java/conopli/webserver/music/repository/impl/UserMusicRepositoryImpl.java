package conopli.webserver.music.repository.impl;

import conopli.webserver.constant.ErrorCode;
import conopli.webserver.exception.ServiceLogicException;
import conopli.webserver.music.entity.UserMusic;
import conopli.webserver.music.repository.UserMusicJpaRepository;
import conopli.webserver.music.repository.UserMusicRepository;
import conopli.webserver.playlist.entity.PlayList;
import conopli.webserver.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserMusicRepositoryImpl implements UserMusicRepository {

    private final UserMusicJpaRepository jpaRepository;

    @Override
    public UserMusic findUserMusicByNum(String num) {
        return jpaRepository.findUserMusicByNum(num)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER_MUSIC));
    }

    @Override
    public Page<UserMusic> findUserMusicByPlayList(PlayList playList, Pageable pageable) {
        return jpaRepository.findUserMusicByPlayList(playList, pageable);
    }

    @Override
    public UserMusic saveUserMusic(UserMusic userMusic) {
        return jpaRepository.save(userMusic);
    }

    @Override
    public void deleteUserMusicByUserMusicId(Long userMusicId) {
        jpaRepository.deleteById(userMusicId);
    }

}
