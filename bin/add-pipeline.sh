#!/bin/bash

#workaround for grrovy limit: it seems groovy can't execute directlly this statement...

curl -H 'Accept: application/vnd.go.cd.v1+json'  -H 'Content-Type: application/json'  -X POST -d @$1 "$2"/go/api/admin/pipelines