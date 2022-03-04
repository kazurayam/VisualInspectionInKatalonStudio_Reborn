CWD=$(pwd)
cd $(mktemp -d)

echo you can visit http://127.0.0.1/

docker run -it -p 3080:8080 kazurayam/flaskr-kazurayam:1.1.0

cd $CWD