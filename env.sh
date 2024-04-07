ENV_FILE=".env"

if test -f "$ENV_FILE"; then
  while IFS='=' read -r key value
  do
    export "$key=$value"
  done < "$ENV_FILE"
fi
export