#include "WindowsPlayerManager.h"

shared_ptr<VideoPlayerManager> VideoPlayerManager::getInstance()
{
	if (instance == nullptr)
	{
		instance = shared_ptr<WindowsPlayerManager>(new WindowsPlayerManager());
	}

	return instance;
}

WindowsPlayerManager::WindowsPlayerManager() {}

WindowsPlayerManager::~WindowsPlayerManager() {}

bool WindowsPlayerManager::open(int videoId, string path) { return false; }

void WindowsPlayerManager::setTexture(int videoId, unsigned int textureId) { }

void WindowsPlayerManager::start(int videoId) {}

void WindowsPlayerManager::update(int videoId) {}

void WindowsPlayerManager::pause(int videoId) {}

void WindowsPlayerManager::stop(int videoId) {}

int WindowsPlayerManager::getWidth(int videoId) { return 0; }

int WindowsPlayerManager::getHeight(int videoId) { return 0; }

void WindowsPlayerManager::destroyAll() {}


