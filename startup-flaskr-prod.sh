CWD=$(pwd)
cd $(mktemp -d)

echo you can visite http://127.0.0.1:3080/

docker run -it -p 3080:8080 kazurayam/flaskr-kazurayam:1.1.0

cd $CWD