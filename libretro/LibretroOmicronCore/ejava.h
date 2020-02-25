#ifndef __EJAVA_H
#define __EJAVA_H

void javaLoop(int ctrlStat, int mx, int my);
void javaSetup();
void javaSetupContext();
void javaTeardown();
void javaLoadGame(const char* path);
int javaSysInfo(int width);
int initJava(retro_log_printf_t logger, const char *omicronJarPath);
int deinitJava();


int fileExists(char* fname);

#endif
