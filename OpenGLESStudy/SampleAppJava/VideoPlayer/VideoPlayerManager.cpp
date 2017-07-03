#include "VideoPlayerManager.h"

shared_ptr<VideoPlayerManager> VideoPlayerManager::instance = nullptr;

VideoPlayerManager::VideoPlayerManager() {}

VideoPlayerManager::~VideoPlayerManager() {}

bool VideoPlayerManager::open(int videoId, string path) { return true; }

void VideoPlayerManager::setTexture(int videoId, unsigned int textureId) { }

void VideoPlayerManager::start(int videoId) {}

void VideoPlayerManager::update(int videoId) {}

void VideoPlayerManager::pause(int videoId) {}

void VideoPlayerManager::stop(int videoId) {}

int VideoPlayerManager::getWidth(int videoId) { return 0; }

int VideoPlayerManager::getHeight(int videoId) { return 0; }

void VideoPlayerManager::destroyAll() {}