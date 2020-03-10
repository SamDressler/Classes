//Sam Dressler
//In-class exercise 3/10/2020
#include <iostream>
#include <sstream>
#include "omp.h"
int main(int argc, char *argv[])
{
#pragma omp parallel
    {
        //Get the thread id
        int id = omp_get_thread_num();
        //Put in buffer to prevent malformation
        std::ostringstream oss;
        //put the message and ID into a string streAM
        oss << "Hello World" << id << std::endl;
        //OUTPUT THE BUFFER
        std::cout << oss.str();
    }
}
