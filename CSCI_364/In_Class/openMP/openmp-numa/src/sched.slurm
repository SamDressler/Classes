#!/bin/bash -l
#
#SBATCH --job-name=sched
#SBATCH --workdir=./
#SBATCH --export=ALL
#SBATCH --exclusive
#SBATCH --time=00:01:00
#
#SBATCH --partition=hodor-cpu
#SBATCH --ntasks=1
#SBATCH --cpus-per-task=4
#
#SBATCH --output=sched.%j.txt

echo "Job start: $(date)"
echo ""

export OMP_NUM_THREADS=$SLURM_CPUS_PER_TASK
echo "Number of OpenMP Threads: $OMP_NUM_THREADS"
srun ./sched
