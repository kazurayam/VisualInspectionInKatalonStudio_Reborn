CWD=$(pwd)
cd $(mktemp -d)

echo going to shutdown a docker container at http://127.0.0.1:3090/

pid=`docker ps --filter publish=3090 --filter status=running -q`

docker stop $pid

cd $CWD