#include <iostream>
#include <thread>
int num_threads = 10;
void hello(int tid) {
    std::cout << "Hello from thread " << tid << std::endl;
}
int main(int argc, char *argv[]) {
    // keep track of the threads
    std::thread t[num_threads];
    // launch the threads
    for (int i = 0; i < num_threads; i++) {
        t[i] = std::thread(hello, i);
    }
    std::cout << "Hello from main" << std::endl;
    // have main wait for the hello threads
    for (int i = 0; i < num_threads; i++) {
        t[i].join();
    }
}
