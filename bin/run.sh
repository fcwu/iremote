#!/bin/sh

WD=`dirname $0`
cd $WD
if [ $# -ne 0 ] && [ "$1" = "start" ]; then
    lsmod | grep -q cdc_acm || insmod ../kernel/cdc-acm.ko
    export PATH=$PATH:$WD
    start-stop-daemon --start --exec $WD/web.py --background
else
    ps aux | grep "$WD/web.py" | awk '{print $2}' | xargs kill -9
    #start-stop-daemon --stop --exec $WD/web.py --retry 30 --oknodo
fi
