package ca.uwaterloo.watform.alloymodel;

import java.util.*;

public class SigScope {

  // optional b/c imports set exactly without a value
  Optional<Integer> value;
  Optional<Boolean> isExact;

  private SigScope(int value, boolean isExact) {
    this.value = Optional.of(value);
    this.isExact = Optional.of(isExact);
  }

  private SigScope(boolean isExact) {
    this.value = Optional.empty();
    this.isExact = Optional.of(isExact);
  }

  private SigScope() {
    this.value = Optional.empty();
    this.isExact = Optional.empty();
  }

  public boolean isExact() {
    return this.isExact.isPresent() && this.isExact.get();
  }

  public boolean hasValue() {
    return this.value.isPresent();
  }

  public Integer getValue() {
    return this.value.get();
  }

  public static SigScope ExactScope(int value) {
    return new SigScope(value, true);
  }

  public static SigScope ExactNoValue() {
    return new SigScope(true);
  }

  public static SigScope NonExactScope(int value) {
    return new SigScope(value, false);
  }

  public static SigScope NoScope() {
    return new SigScope();
  }

  @Override
  public String toString() {
    if (!isExact.isPresent()) return "No Scope";
    return (isExact.get() ? "e" : "") + (value.isPresent() ? value.get() : "?");
  }

  @Override
  public boolean equals(Object obj) {
    // written by ChatGPT
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof SigScope)) {
      return false;
    }

    SigScope other = (SigScope) obj;
    return isExact == other.isExact && Objects.equals(value, other.value);
  }

  @Override
  public int hashCode() {
    // written by ChatGPT
    return Objects.hash(value, isExact);
  }
}
