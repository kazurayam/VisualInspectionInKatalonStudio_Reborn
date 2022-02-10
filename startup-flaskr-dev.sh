CWD=$(pwd)
cd $(mktemp -d)

echo you can visit http://127.0.0.1:3090/

docker run -it -e FLASKR_ALT_VIEW=true -p 3090:8080 kazurayam/flaskr-kazurayam:1.1.0

cd $CWD