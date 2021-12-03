# segwayhack

## Description
Initial starts to self-driving segways (https://en.wikipedia.org/wiki/Segway). The idea being you can request a Segway to self-drive itself to user from it self-charging station and user can ride on it from one side of campus to another. Then, it can self-drive itself back to charging station.

## Code
Used in conjunction with an android app to control a programmable Segway.
- Relies on BING API to detect next point in path to destination
- Uses tensorflow based model to determine sidewalk in camera image to stay on sidewalk or crosswalks. CREDIT: https://github.com/sercant/android-segmentation-app

