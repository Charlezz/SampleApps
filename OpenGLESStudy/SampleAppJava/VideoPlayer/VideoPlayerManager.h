#pragma once

#include <string>
#include <memory>

using namespace std;

class VideoPlayerManager {
public:
	static shared_ptr<VideoPlayerManager> getInstance();

	VideoPlayerManager();
	virtual ~VideoPlayerManager();

	virtual bool open(int videoId, string path);

	virtual void setTexture(int videoId, unsigned int textureId);

	virtual void start(int videoId);

	virtual void pause(int videoId);

	virtual void stop(int videoId);

	virtual void destroyAll();

	virtual int getWidth(int videoId);

	virtual int getHeight(int videoId);

	virtual void update(int videoId);

protected:
	static shared_ptr<VideoPlayerManager> instance;
};
