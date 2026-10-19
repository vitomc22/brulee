#!/usr/bin/env bash
set -euo pipefail

output="${1:?output PDF path required}"
mkdir -p "$(dirname "$output")"
workdir="$(mktemp -d)"
trap 'rm -rf "$workdir"' EXIT

cat > "$workdir/plain.ps" <<'EOF'
%!PS-Adobe-3.0
/Helvetica findfont 18 scalefont setfont
72 740 moveto (Brulee Telie encrypted receipt fixture) show
72 710 moveto (TOTAL 42.00) show
showpage
EOF

gs -q -dBATCH -dNOPAUSE -sDEVICE=pdfwrite \
  -sOwnerPassword=brulee-owner-test \
  -sUserPassword=brulee-test \
  -dEncryptionR=3 -dKeyLength=128 \
  -sOutputFile="$output" "$workdir/plain.ps"
