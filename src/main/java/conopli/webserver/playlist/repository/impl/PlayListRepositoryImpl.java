package conopli.webserver.playlist.repository.impl;

import conopli.webserver.constant.ErrorCode;
import conopli.webserver.exception.ServiceLogicException;
import conopli.webserver.playlist.entity.PlayList;
import conopli.webserver.playlist.repository.PlayListJpaRepository;
import conopli.webserver.playlist.repository.PlayListRepository;
import conopli.webserver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlayListRepositoryImpl implements PlayListRepository {

    private final PlayListJpaRepository jpaRepository;

    @Override
    public PlayList findPlayListById(Long playListId) {
        return jpaRepository.findById(playListId)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_PLAY_LIST));
    }

    @Override
    public List<PlayList> findPlayListByUser(User user) {
        return jpaRepository.findAllByUser(user);
    }

    @Override
    public PlayList savePlayList(PlayList playList) {
        return jpaRepository.save(playList);
    }

    @Override
    public void deletePlayListById(Long playListId) {
        jpaRepository.deleteById(playListId);
    }
}
