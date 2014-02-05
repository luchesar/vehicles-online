if [ -z "$1" ] ; then
  echo "You did not supply a commit message"
  exit 1
fi

git add .
git commit -m "$1"
git push
