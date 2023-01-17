package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.mpaRating.MpaRatingNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {

    MpaRatingStorage mpaRatingStorage;

    @Autowired
    public MpaService(@Qualifier("dbMpaRatingStorage") MpaRatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    public List<MpaRating> getAll() {
        List<MpaRating> mpaRatings =  mpaRatingStorage.getAll();
        log.debug("Запрошено получение всех жанров, получено {} значений", mpaRatings.size());
        return mpaRatings;
    }

    public MpaRating getById(Integer id) {
        log.debug("Запрошено получение MPA-рейтинга с id = {}", id);

        if (!mpaRatingStorage.contains(id)) {
            String errMessage = "MPA-рейтинга с id = " + id + " не существует";
            log.warn(errMessage);
            throw new MpaRatingNotFoundException(errMessage);
        }
        MpaRating mpaRating = mpaRatingStorage.getById(id);
        log.debug("Получено значение {}", mpaRating);
        return mpaRating;
    }

}
