package server.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.common.model.Room;

import java.util.List;

import static server.common.Constants.EndPoint.*;


@RestController
@CrossOrigin("*")
public class RoomController {
    @Autowired
    RoomService roomService;

    //returns list of all rooms
    @GetMapping(ROOMS)
    public List<Room> findAllRooms() {
        return roomService.findAllRooms();
    }

    //list of matching rooms to code
    @GetMapping(ROOM_CODES)
    public List<Room> findByRoomCode(@RequestParam String roomCode) {
        return roomService.findByRoomCode(roomCode);
    }

    //list of marching rooms with the best price deals
    @GetMapping(BEST_PRICE)
    public List<Room> findBestPrice(
            @RequestParam String roomCode,
            @RequestParam String currency) {
        return roomService.findBestPrice(roomCode, currency);
    }







}
