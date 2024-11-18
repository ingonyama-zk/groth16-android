# Groth16 on Icicle: Proof of Concept on Android

## Runing PoC

1. Make sure you have AndroidStudio on your computer

2. Get our PoC from [github](https://github.com/ingonyama-zk/groth16-android)

3. Enable your Android phone for development and run the PoC in AndroidStudio

4. Our PoC expects to find the witness and zkey file in app-specific internal storage:

```txt
/data/data/com.ingonyama.groth16/prover_key.zkey
/data/data/com.ingonyama.groth16/witness.wtns
```

The file names are fixed. You can upload them to your phone using Device Explorer in your AndroidStudio.

## Typical benchmarks

On midrange smartphone (e.g. Samsung A54) the first proof takes ~60 seconds, and subsequent proofs ~30 seconds thanks to caching.
