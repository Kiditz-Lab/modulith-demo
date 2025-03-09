package com.example.demo.orders;

import com.example.demo.shared.LineItemDto;

import java.util.Set;

record OrderPlacedCommand (Set<LineItemDto> items){
}
