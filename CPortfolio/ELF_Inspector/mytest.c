extern int a[];
int b[] = {1, 2, 3};

__attribute((__noinline__)) static int e(int i)
{
  return a[b[i]];
}

int f(int i) {
  return e(i);
}
