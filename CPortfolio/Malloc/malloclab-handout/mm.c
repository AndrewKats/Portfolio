/*
 * Andrew Katsanevas
 * CS 4400
 * 4/10/2017
 *
 * Highest Achieved Performance: 67/100
 * Perf index = 22 (util) + 12 (util_i) + 33 (thru) = 67/100
 *
 * My malloc implementation makes use of an explicit free list (doubly-linked),
 * first fit, and coalescing freed blocks.
 * My first fit works in reverse order, since larger free blocks will more likely be at the end.
 * My free list spans all pages that I map. 
 * If a page becomes completely free, I unmap it.
 * Block headers and footers are 8-bytes each, totalling 16 bytes for alignment.
 * Headers include both an int size and char allocation. Footers are just a size_t size.
 * My prolog contains 16 size and 1 allocation. My epilog includes size 0 and 1 allocation.
 * To account for this 8-byte header, I pad the start of each page with 8 bytes.
 * I use these extra 8 bytes to store the page size multiplier.
 * I use this page size multiplier to create larger and larger pages as I go.
 */
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h>
#include <string.h>

#include "mm.h"
#include "memlib.h"

// Header struct
typedef struct 
{
  int size;
  char allocated;
} block_header;

// Footer struct
typedef struct 
{
  size_t size;
} block_footer;

// Free list node struct
typedef struct list_node
{
  struct list_node* prev;
  struct list_node* next;
} list_node;

// MACROS

/* always use 16-byte alignment */
#define ALIGNMENT 16

/* rounds up to the nearest multiple of ALIGNMENT */
#define ALIGN(size) (((size) + (ALIGNMENT-1)) & ~(ALIGNMENT-1))

#define GET_SIZE_H(p) ((block_header*) p)->size
#define GET_SIZE_F(p) ((block_footer*) p)->size
#define GET_ALLOC(p) ((block_header*) p)->allocated
#define OVERHEAD (sizeof(block_header)+sizeof(block_footer))

#define FTRP(bp) ((char*)(bp)+GET_SIZE_H(HDRP(bp))-OVERHEAD)
#define HDRP(bp) ((char*) (bp) - 8)

#define PREV_BLKP(bp) ((char*)(bp) - GET_SIZE_F((block_footer*)((void*)(bp)-OVERHEAD)))
#define NEXT_BLKP(bp) ((char*)(bp) + GET_SIZE_H(HDRP(bp)))

#define MAX(a, b) (a > b ? a : b)

// Size of page to map. Increases for a while with each page.
size_t pageSize;

// Pointers to the beginning and end of the last mapped page.
void* beginning = NULL;
void* end = NULL;

// First payload of the last mapped page
void* firstPayload = NULL;

// Pointers to each end of the doubly linked free list
list_node* head = NULL;
list_node* tail = NULL;

// Multiplier for the page size. Increases for a while with each page.
int pageMultiplier=5;


// Put a header at the given pointer
void makeHeader(void* ptr, int size, char allocated)
{
  ((block_header*)ptr)->size = size;
  ((block_header*)ptr)->allocated = allocated;
}

// Put a footer at the given pointer
void makeFooter(void* ptr, size_t size)
{
  ((block_footer*)ptr)->size = size;
}


// Add a pointer to the free list
void freeListAdd(list_node* bp)
{
  //printf("\n========>Start free list add at %p\n", bp);
  if(head == NULL)
  {
    //printf("Free list null\n");
    head = bp;
    tail = bp;
    bp->prev = NULL;
    bp->next = NULL;
  }
  else
  {
    //printf("Free list not null\n");
    tail->next = bp;
    bp->prev = tail; 
    bp->next = NULL; 
    tail = bp;
  }
  //printf("\n========>End free list add\n");
}

// Remove a pointer from the free list
void freeListRemove(list_node* bp)
{
  //printf("\n========>Start free list remove at %p\n", bp);
  if(bp == head && bp == tail)
  {
    //printf("1\n");
    head = NULL;
    tail = NULL;
  }
  else if(bp == head)
  {
    //printf("2.1\n");
    bp->next->prev = NULL;
    //printf("2.2\n");
    head = bp->next;
  }
  else if(bp == tail)
  {
    //printf("3\n");
    bp->prev->next = NULL;
    tail = bp->prev;
  }
  else
  {
    //printf("4\n");
    bp->prev->next = bp->next;
    bp->next->prev = bp->prev;
  }
  //printf("\n========>End free list remove\n");
}

// Unmap a page if it is completely free
void check_unmap_page(void* bp)
{
  if(GET_SIZE_H(PREV_BLKP(bp)) == 16)
  {
    //int currentCount = *((int*)(bp-32));
    size_t* currentCount = (size_t*) (PREV_BLKP(bp) - 16);
    //if(GET_SIZE_H(bp) == (mem_pagesize()*currentCount) - 32)
    if(*currentCount == GET_SIZE_H(HDRP(bp)))
    {
      mem_unmap((void*) currentCount, (*currentCount + (2*OVERHEAD)));
      return;
    }
  }

  freeListAdd((list_node*)bp);
}

// Coalesce freed blocks
void coalesce(void* bp)
{
  //printf("\n========>Start coalesce %p, size: %d\n", bp, GET_SIZE_H(HDRP(bp)));
  //printf("Prev block: %p\n", PREV_BLKP(bp));
  //printf("Meh: %d\n", GET_SIZE_F(((block_footer*)(bp-OVERHEAD))));
  //printf("Addr of prev: %p\n", ((char*)(bp)-OVERHEAD));
  //printf("Size of prev: %d\n", GET_SIZE_F(((char*)(bp)-OVERHEAD)));
  //printf("Size of next: %d\n", GET_SIZE_H(HDRP(NEXT_BLKP(bp))));
  //printf("Prev block header: %p\n", HDRP(PREV_BLKP(bp)));


  size_t prev_alloc = GET_ALLOC(HDRP(PREV_BLKP(bp)));
  //printf("f1\n");
  size_t next_alloc = GET_ALLOC(HDRP(NEXT_BLKP(bp)));
  //printf("f2\n");
  size_t size = GET_SIZE_H(HDRP(bp));

  //printf("0\n");
  if(prev_alloc && next_alloc)
  {
    //printf("1s\n");
    //printf("1e\n");
  }
  else if(prev_alloc && !next_alloc)
  {
    //printf("2s\n");
    freeListRemove((list_node*)NEXT_BLKP(bp));
    size += GET_SIZE_H(HDRP(NEXT_BLKP(bp)));
    GET_SIZE_H(HDRP(bp)) = size; 
    makeFooter(FTRP(bp), size);

    //printf("2e\n");
  }
  else if(!prev_alloc && next_alloc)
  {
    //printf("3s\n");
    freeListRemove((list_node*)PREV_BLKP(bp));
    size += GET_SIZE_H(HDRP(PREV_BLKP(bp))); 
    makeFooter(FTRP(bp), size); 
    GET_SIZE_H(HDRP(PREV_BLKP(bp))) = size; 
    bp = PREV_BLKP(bp); 
    //printf("3e\n");
  }
  else
  {
    //printf("4s\n");
    freeListRemove((list_node*)PREV_BLKP(bp));
    freeListRemove((list_node*)NEXT_BLKP(bp));
    size += GET_SIZE_H(HDRP(PREV_BLKP(bp))) + GET_SIZE_F(FTRP(NEXT_BLKP(bp)));  
    GET_SIZE_H(HDRP(PREV_BLKP(bp))) =size; 
    makeFooter(FTRP(NEXT_BLKP(bp)), size); 
    bp = PREV_BLKP(bp); 
    //printf("4e\n");
  }

  //freeListAdd((list_node*)bp);
  check_unmap_page(bp);

  
  //printf("\n========>End coalesce\n");
}

// Map a new page
void new_page()
{
  // Increase page multiplier
  if(pageMultiplier < 32)
  {
    pageMultiplier++;
  }


  pageSize = mem_pagesize()*pageMultiplier;
  beginning = mem_map(pageSize) + 8;

  *((int*)(beginning-8)) = pageMultiplier; 
  

  firstPayload = beginning + 24;
  end = beginning + pageSize - 8;

  freeListAdd(firstPayload);

  // Prolog
  makeHeader(beginning, 16, 1);
  makeFooter(beginning+8, 16);

  // Epilog
  makeHeader(end-8, 0, 1);

  // Set up first block
  makeHeader(HDRP(firstPayload), pageSize-32, 0);
  makeFooter(end-16-8, pageSize-32);
}

/* 
 * mm_init - initialize the malloc package.
 */
int mm_init(void)
{  
  //printf("\n========>Start init\n");
  head = NULL;
  tail = NULL;

  pageMultiplier = 5;

  //pageSize *= pageMultiplier;

  new_page();

  //printf("\n========>End init\n");

  return 0;
}


// Insert a new block, splitting if needed
static void place(void *bp, size_t size)
{
  //printf("\n========>Start place at %p [%d], size: %d\n", bp, bp, size);
  size_t size_needed = ALIGN(size + OVERHEAD);
  //printf("Size needed: %d\n", size_needed);
  size_t size_available = GET_SIZE_H(HDRP(bp));
  size_t size_remainder = size_available - size_needed;

  freeListRemove(bp);

  // Split
  if(size_remainder >= 32)
  {
    // This block
    makeHeader(HDRP(bp), size_needed, 1);
    makeFooter(FTRP(bp), size_needed);

    // New free block
    bp = NEXT_BLKP(bp);
    freeListAdd(bp);
    makeHeader(HDRP(bp), size_remainder, 0);
    makeFooter(FTRP(bp), size_remainder);
  }
  // Don't split
  else
  {
    makeHeader(HDRP(bp), size_available, 1);
  }

  //printf("\n========>End place\n");
}



// Search for the first fit free block
static void* first_fit(size_t size)
{
  //printf("\n========>Start first fit with size: %d\n", size);
  int need_size = MAX(size, sizeof(list_node));
  int new_size = ALIGN(need_size + OVERHEAD);
  //printf("New size: %d\n", new_size);

  list_node* bp = tail;

  while(bp != NULL)
  {
    //printf("bp ff?: %p [%d]", bp, bp);
    //printf(" block: %d %d\n", GET_ALLOC(HDRP(bp)), GET_SIZE_H(HDRP(bp)));
    if(GET_SIZE_H(HDRP(bp)) >= new_size)
    {
      //printf("\nEnd first fit found\n");
      return bp;
    }

    bp = bp->prev;
  }

  //printf("\n========>End first fit not found\n");

  return NULL;
}

/* 
 * mm_malloc - Allocate a block by using bytes from current_avail,
 *     grabbing a new page if necessary.
 */
void *mm_malloc(size_t size)
{  
  //printf("\n========>Start malloc size: %d\n", size);


  void* bp; 

  /* Search the free list for a fit */ 
  if((bp = first_fit(size)) != NULL) 
  { 
    //printf("bp ff: %p [%d]", bp-(beginning-8), bp-(beginning-8));
    place(bp, size); 
    //printf("\n========>End malloc 1\n");
    return bp;
  } 


  //pageMultiplier++;
  //pageSize *= pageMultiplier;

  new_page();

  bp = firstPayload;
  place(bp, size); 

  //printf("\n========>End malloc 2\n");

  return bp; 
}



/*
 * mm_free - Freeing a block does nothing.
 */
void mm_free(void *ptr)
{
  //printf("\n========>Start free %p\n", ptr);

  GET_ALLOC(HDRP(ptr)) = 0; 

  coalesce(ptr);

  //printf("\n========>End free\n");
}