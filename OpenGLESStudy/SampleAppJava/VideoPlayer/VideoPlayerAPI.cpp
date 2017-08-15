#pragma once

#include "include/VideoPlayerAPI.h"
#include "VideoPlayerManager.h"

extern "C"
{
	PLAYER_API bool open(int videoId, const char * path)
	{
		return VideoPlayerManager::getInstance()->open(videoId, path);
	}

	PLAYER_API void setTexture(int videoId, unsigned int textureId)
	{
		VideoPlayerManager::getInstance()->setTexture(videoId, textureId);
	}

	PLAYER_API void play(int videoId)
	{
		VideoPlayerManager::getInstance()->start(videoId);
	}

	PLAYER_API void update(int videoId)
	{
		VideoPlayerManager::getInstance()->update(videoId);
	}

	PLAYER_API void pause(int videoId)
	{
		VideoPlayerManager::getInstance()->pause(videoId);
	}

	PLAYER_API void stop(int videoId)
	{
		VideoPlayerManager::getInstance()->stop(videoId);
	}

	PLAYER_API int getWidth(int videoId)
	{
		return VideoPlayerManager::getInstance()->getWidth(videoId);
	}

	PLAYER_API int getHeight(int videoId)
	{
		return VideoPlayerManager::getInstance()->getHeight(videoId);
	}

	PLAYER_API void destoryAll()
	{
        VideoPlayerManager::getInstance()->destroyAll();
	}
}