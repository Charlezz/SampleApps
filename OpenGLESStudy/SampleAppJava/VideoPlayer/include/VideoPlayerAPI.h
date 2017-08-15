#pragma once

#include "Types.h"

extern "C"
{
	PLAYER_API bool open(int video, const char * path);

	PLAYER_API void setTexture(int video, unsigned int textureId);

	PLAYER_API void play(int video);

	PLAYER_API void update(int video);

	PLAYER_API void pause(int video);

	PLAYER_API void stop(int video);

	PLAYER_API int getWidth(int video);

	PLAYER_API int getHeight(int video);

	PLAYER_API void destoryAll();

}
