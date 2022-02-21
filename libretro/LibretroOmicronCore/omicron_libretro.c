#include <stdio.h>
#include <stdint.h>
#include <stdarg.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#include "glsym/glsym.h"

#include "libretro.h"

#include "ejava.h"

#define ARRAY_SIZE(a) (sizeof(a) / sizeof((a)[0]))
static struct retro_hw_render_callback hw_render;

#if defined(HAVE_PSGL)
#define RARCH_GL_FRAMEBUFFER GL_FRAMEBUFFER_OES
#define RARCH_GL_FRAMEBUFFER_COMPLETE GL_FRAMEBUFFER_COMPLETE_OES
#define RARCH_GL_COLOR_ATTACHMENT0 GL_COLOR_ATTACHMENT0_EXT
#elif defined(OSX_PPC)
#define RARCH_GL_FRAMEBUFFER GL_FRAMEBUFFER_EXT
#define RARCH_GL_FRAMEBUFFER_COMPLETE GL_FRAMEBUFFER_COMPLETE_EXT
#define RARCH_GL_COLOR_ATTACHMENT0 GL_COLOR_ATTACHMENT0_EXT
#else
#define RARCH_GL_FRAMEBUFFER GL_FRAMEBUFFER
#define RARCH_GL_FRAMEBUFFER_COMPLETE GL_FRAMEBUFFER_COMPLETE
#define RARCH_GL_COLOR_ATTACHMENT0 GL_COLOR_ATTACHMENT0
#endif

	int mx = 0;
	int my = 0;
    int width = 320; // just fill in values, games will decide resolution
	int height = 200;
char systemOmicronPath[1024];


#define J_UP (1<<0)
#define J_DOWN (1<<1)
#define J_LEFT (1<<2)
#define J_RIGHT (1<<3)
#define J_A (1<<4)
#define J_B (1<<5)
#define J_X (1<<6)
#define J_Y (1<<7)
#define J_M0 (1<<8)
#define J_M1 (1<<9)
#define J_M2 (1<<10)

static void fallback_log(enum retro_log_level level, const char *fmt, ...)
{
   (void)level;
   va_list va;
   va_start(va, fmt);
   vfprintf(stderr, fmt, va);
   va_end(va);
}


static retro_video_refresh_t video_cb;
static retro_audio_sample_t audio_cb;
static retro_audio_sample_batch_t audio_batch_cb;
static retro_environment_t environ_cb;
static retro_input_poll_t input_poll_cb;
static retro_input_state_t input_state_cb;
retro_log_printf_t log_cb = fallback_log;


void retro_init(void)
{


}

void retro_deinit(void)
{

}

unsigned retro_api_version(void)
{
   return RETRO_API_VERSION;
}

void retro_set_controller_port_device(unsigned port, unsigned device)
{
   log_cb(RETRO_LOG_INFO, "[BACK] retro_set_controller_port_device\n");
   (void)port;
   (void)device;
}

void retro_get_system_info(struct retro_system_info *info)
{


   log_cb(RETRO_LOG_INFO, "[BACK] retro_get_system_info\n");
   
   memset(info, 0, sizeof(*info));
   info->library_name     = "Omicron";
   info->library_version  = "v1";
   info->need_fullpath    = true; // TODO not strictly necessary, change later
   info->valid_extensions = "omicron"; 
   info->block_extract    = false;
   
  
}

void retro_get_system_av_info(struct retro_system_av_info *info)
{
	log_cb(RETRO_LOG_INFO, "[BACK] retro_get_system_av_info\n");
	width = javaSysInfo(1);
	height = javaSysInfo(0);
	log_cb(RETRO_LOG_INFO, "Requested screen size: %d, %d\n", width, height);
   info->timing = (struct retro_system_timing) {
      .fps = 60.0,
      .sample_rate = 0.0,
   };

   info->geometry = (struct retro_game_geometry) {
      .base_width   = width,
      .base_height  = height,
      .max_width    = width,
      .max_height   = height,
      .aspect_ratio = (float)width / (float)height,
   };
   	
}

void retro_set_environment(retro_environment_t cb)
{
   log_cb(RETRO_LOG_INFO, "[BACK] retro_set_environment\n");
   environ_cb = cb;
   if(log_cb == fallback_log)
   {
	   
    struct retro_log_callback logging;
   if (environ_cb(RETRO_ENVIRONMENT_GET_LOG_INTERFACE, &logging))
      log_cb = logging.log;
   else
      log_cb = fallback_log;
    }
   
   log_cb(RETRO_LOG_INFO, "[BACK] SETTING retro_environment_t %d\n", log_cb);

   
      
/*
   struct retro_variable variables[] = {
      {
         "testgl_resolution",
#ifdef HAVE_OPENGLES
         "Internal resolution; 320x240|360x480|480x272|512x384|512x512|640x240|640x448|640x480|720x576|800x600|960x720|1024x768",
#else
         "Internal resolution; 320x240|360x480|480x272|512x384|512x512|640x240|640x448|640x480|720x576|800x600|960x720|1024x768|1024x1024|1280x720|1280x960|1600x1200|1920x1080|1920x1440|1920x1600|2048x2048",
#endif
      },
#ifdef CORE
      { "testgl_multisample", "Multisampling; 1x|2x|4x" },
#endif
      { NULL, NULL },
   };
*/
   bool no_rom = false;
   cb(RETRO_ENVIRONMENT_SET_SUPPORT_NO_GAME, &no_rom);
 //  cb(RETRO_ENVIRONMENT_SET_VARIABLES, variables);
 
  
}




void retro_set_audio_sample(retro_audio_sample_t cb)
{
	log_cb(RETRO_LOG_INFO, "[BACK] retro_set_audio_sample\n");
   audio_cb = cb;
}

void retro_set_audio_sample_batch(retro_audio_sample_batch_t cb)
{
	log_cb(RETRO_LOG_INFO, "[BACK] retro_set_audio_sample_batch\n");
   audio_batch_cb = cb;
}

void retro_set_input_poll(retro_input_poll_t cb)
{
	log_cb(RETRO_LOG_INFO, "[BACK] retro_set_input_poll\n");
   input_poll_cb = cb;
}

void retro_set_input_state(retro_input_state_t cb)
{
	log_cb(RETRO_LOG_INFO, "[BACK] retro_set_input_state\n");
   input_state_cb = cb;
}

void retro_set_video_refresh(retro_video_refresh_t cb)
{
	log_cb(RETRO_LOG_INFO, "[BACK] retro_set_video_refresh\n");
   video_cb = cb;
}

static void update_variables(void)
{
log_cb(RETRO_LOG_INFO, "[BACK] update_variables\n");
	/*
   struct retro_variable var = {
      .key = "testgl_resolution",
   };

   if (environ_cb(RETRO_ENVIRONMENT_GET_VARIABLE, &var) && var.value)
   {
      char *pch;
      char str[100];
      snprintf(str, sizeof(str), "%s", var.value);
      
      pch = strtok(str, "x");
      if (pch)
         width = strtoul(pch, NULL, 0);
      pch = strtok(NULL, "x");
      if (pch)
         height = strtoul(pch, NULL, 0);

      fprintf(stderr, "[libretro-test]: Got size: %u x %u.\n", width, height);
   }
*/
#ifdef CORE
  
#endif
}


static unsigned frame_count;

void retro_run(void)
{
  //log_cb(RETRO_LOG_INFO, "[BACK] retro_run\n");
   bool updated = false;
   if (environ_cb(RETRO_ENVIRONMENT_GET_VARIABLE_UPDATE, &updated) && updated)
      update_variables();

  //log_cb(RETRO_LOG_INFO, "[BACK] input poll\n");

   input_poll_cb();
	unsigned int ctrlStat = 0;
	int player = 0;
	
	if (input_state_cb(player, RETRO_DEVICE_JOYPAD, 0, RETRO_DEVICE_ID_JOYPAD_UP)) ctrlStat |= J_UP;
	if (input_state_cb(player, RETRO_DEVICE_JOYPAD, 0, RETRO_DEVICE_ID_JOYPAD_DOWN)) ctrlStat |= J_DOWN;
	if (input_state_cb(player, RETRO_DEVICE_JOYPAD, 0, RETRO_DEVICE_ID_JOYPAD_LEFT)) ctrlStat |= J_LEFT;
	if (input_state_cb(player, RETRO_DEVICE_JOYPAD, 0, RETRO_DEVICE_ID_JOYPAD_RIGHT)) ctrlStat |= J_RIGHT;


	if (input_state_cb(player, RETRO_DEVICE_JOYPAD, 0, RETRO_DEVICE_ID_JOYPAD_A)) ctrlStat |= J_A;
	if (input_state_cb(player, RETRO_DEVICE_JOYPAD, 0, RETRO_DEVICE_ID_JOYPAD_B)) ctrlStat |= J_B;
	if (input_state_cb(player, RETRO_DEVICE_JOYPAD, 0, RETRO_DEVICE_ID_JOYPAD_X)) ctrlStat |= J_X;
	if (input_state_cb(player, RETRO_DEVICE_JOYPAD, 0, RETRO_DEVICE_ID_JOYPAD_Y)) ctrlStat |= J_Y;


// Mouse buttons.

	if (input_state_cb(0, RETRO_DEVICE_MOUSE, 0, RETRO_DEVICE_ID_MOUSE_LEFT)) 
	{
		ctrlStat |= J_M0;
	}
	if (input_state_cb(0, RETRO_DEVICE_MOUSE, 0, RETRO_DEVICE_ID_MOUSE_MIDDLE)) ctrlStat |= J_M1;
	if (input_state_cb(0, RETRO_DEVICE_MOUSE, 0, RETRO_DEVICE_ID_MOUSE_RIGHT)) ctrlStat |= J_M2;


	//printf(ctrlStat);
	mx += input_state_cb(0, RETRO_DEVICE_MOUSE, 0, RETRO_DEVICE_ID_MOUSE_X) / 2;
	my += input_state_cb(0, RETRO_DEVICE_MOUSE, 0, RETRO_DEVICE_ID_MOUSE_Y) / 2;

	if(mx<0) mx=0;
	if(my<0) my=0;
	if(mx>=width) mx = width-1;
	if(my>=height) my = height-1;
#ifdef CORE
#endif
    //log_cb(RETRO_LOG_INFO, "[BACK] binding backbuffer\n");

      
   glBindFramebuffer(RARCH_GL_FRAMEBUFFER, hw_render.get_current_framebuffer());
    // log_cb(RETRO_LOG_INFO, "[BACK] starting java loop\n");

   javaLoop(ctrlStat, mx, my);

  //log_cb(RETRO_LOG_INFO, "[BACK] java loop done\n");

   frame_count++;

   video_cb(RETRO_HW_FRAME_BUFFER_VALID, width, height, 0);
   
   //log_cb(RETRO_LOG_INFO, "[BACK] retro_run finished\n");
   // glBindFramebuffer(RARCH_GL_FRAMEBUFFER, 0); // TODO needed ?
}

static void context_reset(void)
{

   log_cb(RETRO_LOG_INFO, "[BACK] Context reset!\n");
   rglgen_resolve_symbols(hw_render.get_proc_address);
   javaResetContext();
   //javaSetup(); 
	
}

static void context_destroy(void)
{
   log_cb(RETRO_LOG_INFO, "[BACK] Context destroy!\n");
   javaContextDestroy();
   log_cb(RETRO_LOG_INFO, "[BACK] Called javaContextDestroy!\n");
}

#ifdef HAVE_OPENGLES
static bool do_init_hw_context(void)
{
	log_cb(RETRO_LOG_INFO, "retro_init_hw_context 1!\n");
#if defined(HAVE_OPENGLES_3_1)
   hw_render.context_type = RETRO_HW_CONTEXT_OPENGLES_VERSION;
   hw_render.version_major = 3;
   hw_render.version_minor = 1;
#elif defined(HAVE_OPENGLES3)
   hw_render.context_type = RETRO_HW_CONTEXT_OPENGLES3;
#else
   hw_render.context_type = RETRO_HW_CONTEXT_OPENGLES2;
#endif
   hw_render.context_reset = context_reset;
   hw_render.context_destroy = context_destroy;
   hw_render.depth = true;
   hw_render.stencil = true;
   hw_render.bottom_left_origin = true;

	log_cb(RETRO_LOG_INFO, "[cc] RETRO_ENVIRONMENT_SET_HW_RENDER \n");
   if (!environ_cb(RETRO_ENVIRONMENT_SET_HW_RENDER, &hw_render))
   {
   		log_cb(RETRO_LOG_INFO, "[cc] RETRO_ENVIRONMENT_SET_HW_RENDER done\n");
      return false;
	}	
   return true;
}
#else
static bool do_init_hw_context(void)
{
	log_cb(RETRO_LOG_INFO, "retro_init_hw_context 2\n");
#if defined(CORE)

#else
   hw_render.context_type = RETRO_HW_CONTEXT_OPENGL;
#endif
   hw_render.context_reset = context_reset;
   hw_render.context_destroy = context_destroy;
   hw_render.depth = false;
   hw_render.stencil = false;
   hw_render.bottom_left_origin = true;
   hw_render.debug_context = true;
   hw_render.cache_context = false;

	log_cb(RETRO_LOG_INFO, "[cc] RETRO_ENVIRONMENT_SET_HW_RENDER \n");
   if (!environ_cb(RETRO_ENVIRONMENT_SET_HW_RENDER, &hw_render)){
   	log_cb(RETRO_LOG_ERROR, "[cc] RETRO_ENVIRONMENT_SET_HW_RENDER done BAD\n");
   	return false;
   }
      log_cb(RETRO_LOG_INFO, "[cc] RETRO_ENVIRONMENT_SET_HW_RENDER done\n");

   return true;
}
#endif

bool retro_load_game(const struct retro_game_info *info)
{
	log_cb(RETRO_LOG_INFO, "[BACK] retro_load_game\n");
    update_variables();
    
    
   char *system;
   if (environ_cb(RETRO_ENVIRONMENT_GET_SYSTEM_DIRECTORY, &system) && system)
   {
   		// ok
   }
   else
   {
   		log_cb(RETRO_LOG_ERROR, "Could not get system directory\n");
   		return false;
   }
   
   log_cb(RETRO_LOG_INFO, "System folder is: %s\n", system);
   int s = snprintf(systemOmicronPath, 1024, "%s/%s", system, "omicron/omicron.jar");
   if(!s)
   {
   	log_cb(RETRO_LOG_ERROR, "Could not concatenate dirs\n");
   	
   	return false;
   }
   log_cb(RETRO_LOG_INFO, "Omicron location is: %s\n", systemOmicronPath);
   if(!fileExists(systemOmicronPath))
   {
   		log_cb(RETRO_LOG_ERROR, "Could not find omicron.jar in %s\n", systemOmicronPath);
   		return false;
   }
	
    
	if (initJava(&systemOmicronPath) != 0)
	{
		log_cb(RETRO_LOG_ERROR, "Could not initialize JAVA\n");
		return false;
	}
	
/*   struct retro_message msg;
   msg.frames = 600;
   msg.msg = "Hello there i am a message";
   environ_cb(RETRO_ENVIRONMENT_SET_MESSAGE, &msg);
*/
   enum retro_pixel_format fmt = RETRO_PIXEL_FORMAT_XRGB8888;
   if (!environ_cb(RETRO_ENVIRONMENT_SET_PIXEL_FORMAT, &fmt))
   {
      log_cb(RETRO_LOG_ERROR, "XRGB8888 is not supported.\n");
      return false;
   }

   if (!do_init_hw_context())
   {
      log_cb(RETRO_LOG_ERROR, "HW Context could not be initialized, exiting...\n");
      return false;
   }

   if(info)
   {
   		javaLoadGame(info->path);
   }
   log_cb(RETRO_LOG_INFO, "Loaded game!\n");
   (void)info;
   return true;
}

void retro_unload_game(void)
{
	log_cb(RETRO_LOG_INFO, "[BACK] retro_unload_game\n");
	javaUnloadGame();
}

unsigned retro_get_region(void)
{
	log_cb(RETRO_LOG_INFO, "[BACK] retro_get_region\n");
   return RETRO_REGION_NTSC;
}

bool retro_load_game_special(unsigned type, const struct retro_game_info *info, size_t num)
{
   log_cb(RETRO_LOG_INFO, "[BACK] retro_load_game_special\n");
   (void)type;
   (void)info;
   (void)num;
   return false;
}

size_t retro_serialize_size(void)
{
   return 0;
}

bool retro_serialize(void *data, size_t size)
{
   (void)data;
   (void)size;
   return false;
}

bool retro_unserialize(const void *data, size_t size)
{
   (void)data;
   (void)size;
   return false;
}

void *retro_get_memory_data(unsigned id)
{
   (void)id;
   return NULL;
}

size_t retro_get_memory_size(unsigned id)
{
   (void)id;
   return 0;
}

void retro_reset(void)
{
log_cb(RETRO_LOG_INFO, "[BACK] retro_reset\n");
}

void retro_cheat_reset(void)
{
log_cb(RETRO_LOG_INFO, "[BACK] retro_cheat_reset\n");
}

void retro_cheat_set(unsigned index, bool enabled, const char *code)
{
   (void)index;
   (void)enabled;
   (void)code;
}

