package com.coinx.core.controller;


import com.coinx.core.model.User;
import com.coinx.core.model.Wallet;
import com.coinx.core.repository.UserRepository;
import com.coinx.core.repository.WalletRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public WalletController(WalletRepository walletRepository,  UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }


    @PostMapping("/{userId}")
    public ResponseEntity<Wallet> createWallet(@PathVariable UUID userId, @RequestBody Wallet walletData){
       Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        User foundUser = userOptional.get();
        walletData.setUser(foundUser);

        Wallet saveWallet = walletRepository.save(walletData);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveWallet);
    }

    @GetMapping
    public ResponseEntity<List<Wallet>> findAll() {
        return ResponseEntity.ok(walletRepository.findAll());
    }

}
