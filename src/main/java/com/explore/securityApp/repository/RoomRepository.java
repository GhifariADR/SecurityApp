package com.explore.securityApp.repository;

import com.explore.securityApp.entity.Room;
import com.explore.securityApp.enums.BookingStatus;
import com.explore.securityApp.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByName(String name);

    List<Room> findAllByStatus(RoomStatus status);
}
