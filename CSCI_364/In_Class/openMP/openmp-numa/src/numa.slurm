#!/bin/bash -l
#
#SBATCH --job-name=numa
#SBATCH --workdir=./
#SBATCH --export=ALL
#SBATCH --exclusive
#SBATCH --time=00:01:00
#
#SBATCH --partition=hodor-cpu
#SBATCH --ntasks=1
#SBATCH --cpus-per-task=8
#
#SBATCH --output=numa.%j.txt

echo "Job start: $(date)"
echo ""

export OMP_NUM_THREADS=$SLURM_CPUS_PER_TASK
echo "Number of OpenMP Threads: $OMP_NUM_THREADS"
srun ./numa
