package conopli.webserver.playlist.repository;

import conopli.webserver.playlist.entity.PlayList;
import conopli.webserver.user.entity.User;

import java.util.List;

public interface PlayListRepository {

    PlayList findPlayListById(Long playListId);

    List<PlayList> findPlayListByUser(User user);

    PlayList savePlayList(PlayList playList);

    void deletePlayListById(Long playListId);
}
