#!/bin/bash

echo $FLAG >/flag_is_is_here
echo $FLAG
FLAG=not_flag_xixi
export FLAG=not_flag_xixi
echo $FLAG
java -jar SleepWalker.jar


