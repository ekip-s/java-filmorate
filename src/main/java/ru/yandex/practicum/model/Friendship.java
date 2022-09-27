package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    private long userMaster;
    private long userSlave;
    private boolean isConfirmed;
}