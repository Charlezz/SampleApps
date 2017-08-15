#pragma once

#if (defined WIN32) && defined VIDEOPLAYER_EXPORTS
#  define PLAYER_API __declspec(dllexport)
#else
#  define PLAYER_API
#endif