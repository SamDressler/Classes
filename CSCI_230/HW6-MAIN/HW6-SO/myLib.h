//Sam dressler
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int SCAN (FILE *(*stream));
struct _data* LOAD (FILE *stream, int size);
void SEARCH (struct _data *BlackBox, char *name, int size);
void FREE (struct _data *BlackBox, int size);
