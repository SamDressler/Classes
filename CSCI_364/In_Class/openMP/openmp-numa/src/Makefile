CXX = g++
ONETOUCH = -DONETOUCH
CXXFLAGS = -g -Wall -std=c++11 -O0 -fopenmp $(ONETOUCH)

INCLUDES = 

LDFLAGS = 
LDLIBS = 

# clear any suffixes
.SUFFIXES:
# set suffixes
.SUFFIXES: .cpp .o

all: sched numa
	@echo sched and numa compiled

# implicit rule: build an executable x from x.cpp
.cpp:
	$(CXX) $(CXXFLAGS) $(INCLUDES) $< -o $@ $(LDFLAGS) $(LDLIBS)

.PHONY: clean
clean:
	$(RM) -r sched numa *.dSYM
