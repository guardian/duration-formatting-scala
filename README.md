# duration-formatting
_Formatting [`java.time.Duration`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Duration.html)_

## Formatting a duration range

The library can format duration _ranges_ (eg `10-25s`) in a more pleasing & concise way,
using a single common unit where possible:

* `20–25s`, rather than `20s-25s`  (the common unit is _seconds_)
* `1h` rather than `60m`... yet `59-60m`, rather than `59m-1h` (automatically choosing a common unit of _minutes_)
* `55s–1m25s` rather than `55–85s`

## Alternative styles 

Not provided by this library, but still interesting:

### [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601#Durations)

Durations are formatted as `PnYnMnDTnHnMnS`, eg `P23DT23H` - this is the default format used
by `java.time.Duration.toString()`.

### What does the [Guardian style guide](https://www.theguardian.com/guardian-style-guide-t) say?

> **times**
> 
> _1am, 6.30pm, etc; 10 o’clock last night but 10pm yesterday; half past two, a quarter to three, 10 to 11, etc;_
> **2hr 5min 6sec**, etc; _for 24-hour clock, 00.47, 23.59; noon, midnight (not 12 noon, 12 midnight or 12am, 12pm)._

...we don't actually do this in the `duration-formatting` library, where we're focusing more on conciseness
in readouts rather than prose, but it wouldn't be hard to add an adapter if we wanted to.
