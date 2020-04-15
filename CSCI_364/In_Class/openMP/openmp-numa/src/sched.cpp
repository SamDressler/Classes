/*
 * test.cpp
 *
 *  Created on: Apr 5, 2020
 *      Author: david
 */

#include <iostream>
#include <omp.h>
#include <sstream>

int main(int argc, char *argv[]) {
	using namespace std;

	cout << "Scheduling Demo " << endl;

#pragma omp parallel
	{
	int num_threads = omp_get_num_threads();
	ostringstream oss;

#pragma omp master
	{
	oss << "num_threads: " << num_threads << endl;
	cout << oss.str();
	oss.str("");
	}

#pragma omp barrier

#pragma omp for schedule(static, 1)
	for (int i = 0; i < num_threads; i++) {
		oss << "[1]  i: " << i << ", tid: " << omp_get_thread_num() << endl;
		cout << oss.str();
	}

	/*
	 * Same number of iterations.
	 * Same number of threads.
	 * Will the iterations be mapped to the threads as before?
	 */
#pragma omp for schedule(static, 1)
	for (int i = 0; i < num_threads; i++) {
		oss.str("");
		oss << "[2]  i: " << i << ", tid: " << omp_get_thread_num() << endl;
		cout << oss.str();
	}
	} // end omp parallel

	cout << endl;

	/*
     * A different parallel region.
	 * Same number of iterations.
	 * Same number of threads.
	 * Will the iterations be mapped to the threads as before?
	 */
#pragma omp parallel
	{
	int num_threads = omp_get_num_threads();
	ostringstream oss;

#pragma omp for schedule(static, 1)
	for (int i = 0; i < num_threads; i++) {
		oss << "[3]  i: " << i << ", tid: " << omp_get_thread_num() << endl;
		cout << oss.str();
	}
	} // end omp parallel
}
