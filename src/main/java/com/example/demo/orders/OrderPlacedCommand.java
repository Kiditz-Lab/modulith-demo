package com.example.demo.orders;

import java.util.Set;

record OrderPlacedCommand (Set<LineItemDto> items){
}
