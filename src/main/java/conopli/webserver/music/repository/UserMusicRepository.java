package conopli.webserver.music.repository;

import conopli.webserver.music.entity.UserMusic;
import conopli.webserver.playlist.entity.PlayList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserMusicRepository {

    UserMusic findUserMusicByNum(String num);

    Page<UserMusic> findUserMusicByPlayList(PlayList playList, Pageable pageable);

    UserMusic saveUserMusic(UserMusic userMusic);

    void deleteUserMusicByUserMusicId(Long userMusicId);
}
