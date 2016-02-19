# add the key used by GO pipelines
# to use fleetctl remotly

rm /tmp/GO_SSH_ANGENT.sck
eval `ssh-agent -s -a /tmp/GO_SSH_ANGENT.sck`
ssh-add /anarchia/certs/id_rsa_anarchia_go
