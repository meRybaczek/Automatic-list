package project.backend.access;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AccessController {

    @Autowired
    private final AccessService accessService;

    @GetMapping("/{gateId}/")
    public AccessResponse logAccess(@RequestParam String uid, @PathVariable Integer gateId){
        return accessService.logAccess(uid, gateId);
    }

    @GetMapping("/{gateId}/lastLogInfo/")
    public AccessResponse getLastLog(@RequestParam String uid, @PathVariable Integer gateId){
        return accessService.getLastLog(uid);
    }

}
