DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
export ANARCHIADIR=$DIR/..
if [ -d "/anarchia" ]
then
	ANARCHIADIR=/anarchia
fi

echo $ANARCHIADIR

docker run -p 5000:5000 -d  \
   -v $ANARCHIADIR/certs:/certs \
   -v $ANARCHIADIR/work:/var/lib/registry \
   -e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/domain.crt \
   -e REGISTRY_HTTP_TLS_KEY=/certs/domain.key \
   registry:2
