package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM meal WHERE id=:id AND user_id=:userId")
    Optional<Meal> findByIdAndUserId(@Param("id") int id, @Param("userId") int userId);

    @Query(nativeQuery = true, value = "DELETE FROM meal WHERE id=:id AND user_id=:userId")
    @Modifying
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query(nativeQuery = true, value = "SELECT * FROM meal WHERE user_id=:userId ORDER BY date_time DESC")
    List<Meal> findAllByUserId(@Param("userId") int userId);

    @Query(nativeQuery = true, value = "SELECT * FROM meal WHERE user_id=:userId " +
            "AND date_time>=:startDateTime " +
            "AND date_time<:endDateTime " +
            "ORDER BY date_time DESC")
    List<Meal> findBetweenHalfOpen(@Param("startDateTime") LocalDateTime startDateTime,
                                   @Param("endDateTime") LocalDateTime endDateTime,
                                   @Param("userId") int userId);
}
