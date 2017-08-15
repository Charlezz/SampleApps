#pragma once

#include "VideoPlayerManager.h"

class WindowsPlayerManager : public VideoPlayerManager
{
public:
	WindowsPlayerManager();
	
	~WindowsPlayerManager();

	bool open(int videoId, string path);

	void setTexture(int videoId, unsigned int textureId);

	void start(int videoId);

	void update(int videoId);

	void pause(int videoId);

	void stop(int videoId);

	int getWidth(int videoId);

	int getHeight(int videoId);
		
	void destroyAll();
};
