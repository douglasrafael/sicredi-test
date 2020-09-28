package com.fsdevelopment.sicreditestapp.mocks

import com.fsdevelopment.sicreditestapp.data.model.Event

val event1 = Event(
    id = "1",
    title = "Lorem 1",
    description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
    date = 1601143592487,
    price = 29.90,
    image = "https://test1.jpg",
    longitude = "-7.230950",
    latitude = "-35.881889",
    people = arrayListOf(people1, people2)
)

val event2 = Event(
    id = "2",
    title = "Lorem 2",
    description = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
    date = 1606568400000,
    price = 29.90,
    image = "https://test2.jpg",
    longitude = "-7.115250",
    latitude = "-34.861050",
    people = arrayListOf(people3)
)

val event3 = Event(
    id = "3",
    title = "Lorem 3",
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
    date = 1613430000000,
    price = 29.90,
    image = "https://test1.jpg",
    longitude = "-19.917299",
    latitude = "-43.934559",
    people = arrayListOf()
)
