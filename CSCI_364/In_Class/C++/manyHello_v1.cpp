#include <iostream>
#include <thread>
void hello(){
    std::cout << "Hello, World" << std::endl;

}
int main(int argc, char *argv[]){
    //launch the thread
    std::thread t1(hello);
    
    t1.join();
    
    return 0;
}
