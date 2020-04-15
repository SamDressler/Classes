/*
 * numa.cpp
 *
 *  Created on: Apr 6, 2020
 *      Author: david
 */

#include <cstdlib>
#include <cstring>
#include <iostream>
#include <omp.h>
#include <sstream>

int main(int argc, char *argv[]) {
	using namespace std;

	// malloc reserves memory but does not "touch" the physical memory
	char *a = (char *)malloc(8 * 4096);
#ifdef ONETOUCH
    memset(&a[0], 0, 8 * 4096);
    //for (int i = 0; i < 8*4096; i++)
    //    a[i] = 0;
#endif
    const int ITER = 10;

#pragma omp parallel
	{
	int num_threads = omp_get_num_threads();
	double start_time;
	double end_time;
	ostringstream oss;

#pragma omp for schedule(static, 1)
	// each thread initializes (or "touches") a page of memory
	for (int i = 0; i < num_threads; i++) {
		start_time = omp_get_wtime();
        for (int x = 0; x < ITER; x++)
		    memset(&a[i*4096], 0, 4096);
		end_time = omp_get_wtime();

        double time = (end_time - start_time) / ITER;
		oss << "[1]  i: " << i << ", tid: " << omp_get_thread_num()
				<< ", time: " << time << endl;
		cout << oss.str();
	}
#ifndef ONETOUCH
#pragma omp for schedule(static, 1)
	for (int i = 0; i < num_threads; i++) {
		start_time = omp_get_wtime();
        for (int x = 0; x < ITER; x++)
		    memset(&a[i*4096], 1, 4096);
		end_time = omp_get_wtime();

        double time = (end_time - start_time) / ITER;
        oss.str("");
		oss << "[2]  i: " << i << ", tid: " << omp_get_thread_num()
				<< ", time: " << time << endl;
		cout << oss.str();
	}
#endif
	}
}
