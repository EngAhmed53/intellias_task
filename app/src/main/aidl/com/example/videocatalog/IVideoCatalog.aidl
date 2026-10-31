package com.example.videocatalog;

import com.example.videocatalog.VideoInfo;

interface IVideoCatalog {
    List<VideoInfo> listVideos();
    int getSerivcePID();
}
