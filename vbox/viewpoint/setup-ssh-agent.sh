# add the key used by GO pipelines
# to use fleetctl remotly

if [ -f /tmp/GO_SSH_ANGENT.sck ]
then
  rm /tmp/GO_SSH_ANGENT.sck
fi

eval `ssh-agent -s -a /tmp/GO_SSH_ANGENT.sck`
ssh-add /anarchia/certs/id_rsa_anarchia_go
