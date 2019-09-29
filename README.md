# BlockFlow
![Demo at dev.aaaaahhhhhhh.com](https://i.imgur.com/Q53i7rc.png)

## Goal
BlockFlow's goal is to visualize data from various sources in a more interactive user friendly manner. It aims to inform the general public about statistics through Minecraft.

## Implementation
For this particular project I chose to represent statistical data about the world, such as the average height of men and women, as well as the population per area over time. I used an SVG of the world to designate which areas of the map would light up and such. The data was retrieved from various online databases, then merged together into one CSV file. The handling of the visual representation was done serverside using the Bukkit API, a serverside Minecraft modding software.

## Restrictions
Due to the limitations imposed by the Minecraft modding software, and the 24 hour time limit, I was not able to implement every feature I wanted. Visualizations have to be controlled manually and the code is not completely optimized, but it serves its purpose well.

## Commands
/blockflow create
/blockflow select <region>
/blockflow deselect
/blockflow clear

## Team members
- Daniel Gu
- Zhaxi Zerong
