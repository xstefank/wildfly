#!/usr/bin/zsh

ITER=0

while : ; do
  ITER=$[ITER + 1]
  echo "------------- Iteration $ITER"
  mvn clean install -DtestLogToFile=false
  [[ `echo $?` == 0 ]] || break
done

echo "Failed after $ITER iterations"
echo "$ITER" >> fails.txt
