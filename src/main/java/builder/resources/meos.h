//-------------------- meos.h --------------------


//#include <stdbool.h>
//#include <stdint.h>
//######
typedef char *Pointer;
typedef uintptr_t Datum;

typedef signed char int8;
typedef signed short int16;
typedef signed int int32;
typedef long int int64;

typedef unsigned char uint8;
typedef unsigned short uint16;
typedef unsigned int uint32;
typedef unsigned long int uint64;

typedef int32 DateADT;
typedef int64 TimeADT;
typedef int64 Timestamp;
typedef int64 TimestampTz;
typedef int64 TimeOffset;
typedef int32 fsec_t;      

typedef struct
{
  TimeOffset time;  
  int32 day;        
  int32 month;      
} Interval;

typedef struct varlena
{
  char vl_len_[4];  
  char vl_dat[];    
} varlena;

typedef varlena text;
typedef struct varlena bytea;

              

typedef uint16_t lwflags_t;

typedef struct {
    double afac, bfac, cfac, dfac, efac, ffac, gfac, hfac, ifac, xoff, yoff, zoff;
} AFFINE;

typedef struct
{
    double xmin, ymin, zmin;
    double xmax, ymax, zmax;
    int32_t srid;
}
BOX3D;

typedef struct
{
    lwflags_t flags;
    double xmin;
    double xmax;
    double ymin;
    double ymax;
    double zmin;
    double zmax;
    double mmin;
    double mmax;
} GBOX;

typedef struct
{
    double  a;  
    double  b;  
    double  f;  
    double  e;  
    double  e_sq;   
    double  radius;  
    char    name[20];  
}
SPHEROID;

typedef struct
{
    double x, y;
}
POINT2D;

typedef struct
{
    double x, y, z;
}
POINT3DZ;

typedef struct
{
    double x, y, z;
}
POINT3D;

typedef struct
{
    double x, y, m;
}
POINT3DM;

typedef struct
{
    double x, y, z, m;
}
POINT4D;

typedef struct
{
    uint32_t npoints;   
    uint32_t maxpoints; 

    
    lwflags_t flags;

    
    uint8_t *serialized_pointlist;
}
POINTARRAY;

typedef struct
{
    uint32_t size; 
    uint8_t srid[3]; 
    uint8_t gflags; 
    uint8_t data[1]; 
} GSERIALIZED;

typedef struct
{
    GBOX *bbox;
    void *data;
    int32_t srid;
    lwflags_t flags;
    uint8_t type;
    char pad[1]; 
}
LWGEOM;

typedef struct
{
    GBOX *bbox;
    POINTARRAY *point;  
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
}
LWPOINT; 

typedef struct
{
    GBOX *bbox;
    POINTARRAY *points; 
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
}
LWLINE; 

typedef struct
{
    GBOX *bbox;
    POINTARRAY *points;
    int32_t srid;
    lwflags_t flags;
    uint8_t type;
    char pad[1]; 
}
LWTRIANGLE;

typedef struct
{
    GBOX *bbox;
    POINTARRAY *points; 
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
}
LWCIRCSTRING; 

typedef struct
{
    GBOX *bbox;
    POINTARRAY **rings; 
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
    uint32_t nrings;   
    uint32_t maxrings; 
}
LWPOLY; 

typedef struct
{
    GBOX *bbox;
    LWPOINT **geoms;
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
    uint32_t ngeoms;   
    uint32_t maxgeoms; 
}
LWMPOINT;

typedef struct
{
    GBOX *bbox;
    LWLINE **geoms;
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
    uint32_t ngeoms;   
    uint32_t maxgeoms; 
}
LWMLINE;

typedef struct
{
    GBOX *bbox;
    LWPOLY **geoms;
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
    uint32_t ngeoms;   
    uint32_t maxgeoms; 
}
LWMPOLY;

typedef struct
{
    GBOX *bbox;
    LWGEOM **geoms;
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
    uint32_t ngeoms;   
    uint32_t maxgeoms; 
}
LWCOLLECTION;

typedef struct
{
    GBOX *bbox;
    LWGEOM **geoms;
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
    uint32_t ngeoms;   
    uint32_t maxgeoms; 
}
LWCOMPOUND; 

typedef struct
{
    GBOX *bbox;
    LWGEOM **rings;
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
    uint32_t nrings;    
    uint32_t maxrings;  
}
LWCURVEPOLY; 

typedef struct
{
    GBOX *bbox;
    LWGEOM **geoms;
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
    uint32_t ngeoms;   
    uint32_t maxgeoms; 
}
LWMCURVE;

typedef struct
{
    GBOX *bbox;
    LWGEOM **geoms;
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
    uint32_t ngeoms;   
    uint32_t maxgeoms; 
}
LWMSURFACE;

typedef struct
{
    GBOX *bbox;
    LWPOLY **geoms;
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
    uint32_t ngeoms;   
    uint32_t maxgeoms; 
}
LWPSURFACE;

typedef struct
{
    GBOX *bbox;
    LWTRIANGLE **geoms;
    int32_t srid;
    lwflags_t flags;
    uint8_t type; 
    char pad[1]; 
    uint32_t ngeoms;   
    uint32_t maxgeoms; 
}
LWTIN;

extern LWPOINT *lwpoint_make(int32_t srid, int hasz, int hasm, const POINT4D *p);

extern LWGEOM *lwgeom_from_gserialized(const GSERIALIZED *g);
extern GSERIALIZED *gserialized_from_lwgeom(LWGEOM *geom, size_t *size);

extern int32_t lwgeom_get_srid(const LWGEOM *geom);

extern double lwpoint_get_x(const LWPOINT *point);
extern double lwpoint_get_y(const LWPOINT *point);
extern double lwpoint_get_z(const LWPOINT *point);
extern double lwpoint_get_m(const LWPOINT *point);

extern int lwgeom_has_z(const LWGEOM *geom);
extern int lwgeom_has_m(const LWGEOM *geom);

              

typedef struct
{
  int32 vl_len_;        
  uint8 settype;        
  uint8 basetype;       
  int16 flags;          
  int32 count;          
  int32 maxcount;       
  int16 bboxsize;       
} Set;

typedef struct
{
  uint8 spantype;       
  uint8 basetype;       
  bool lower_inc;       
  bool upper_inc;       
  Datum lower;          
  Datum upper;          
} Span;

typedef struct
{
  int32 vl_len_;        
  uint8 spansettype;    
  uint8 spantype;       
  uint8 basetype;       
  char padding;         
  int32 count;          
  int32 maxcount;       
  Span span;            
  Span elems[1];        
} SpanSet;

typedef struct
{
  Span period;          
  Span span;            
  int16 flags;          
} TBox;

typedef struct
{
  Span period;          
  double xmin;          
  double xmax;          
  double ymin;          
  double ymax;          
  double zmin;          
  double zmax;          
  int32  srid;          
  int16  flags;         
} STBox;

typedef enum
{
  INTERP_NONE =    0,
  DISCRETE =       1,
  STEP =           2,
  LINEAR =         3,
} interpType;

typedef enum
{
  INTERSECTS =     0,
  CONTAINS =       1,
  TOUCHES =        2,
} spatialRel;

typedef struct
{
  int32 vl_len_;        
  uint8 temptype;       
  uint8 subtype;        
  int16 flags;          
  
} Temporal;

typedef struct
{
  int32 vl_len_;        
  uint8 temptype;       
  uint8 subtype;        
  int16 flags;          
  TimestampTz t;        
  Datum value;          
  
} TInstant;

typedef struct
{
  int32 vl_len_;        
  uint8 temptype;       
  uint8 subtype;        
  int16 flags;          
  int32 count;          
  int32 maxcount;       
  int16 bboxsize;       
  char padding[6];      
  Span period;          
  
} TSequence;

typedef struct
{
  int32 vl_len_;        
  uint8 temptype;       
  uint8 subtype;        
  int16 flags;          
  int32 count;          
  int32 totalcount;     
  int32 maxcount;       
  int16 bboxsize;       
  int16 padding;        
  Span period;          
  
} TSequenceSet;

typedef struct
{
  int i;
  int j;
} Match;

#define SKIPLIST_MAXLEVEL 32
typedef struct
{
  void *value;
  int height;
  int next[SKIPLIST_MAXLEVEL];
} SkipListElem;

typedef struct
{
  int capacity;
  int next;
  int length;
  int *freed;
  int freecount;
  int freecap;
  int tail;
  void *extra;
  size_t extrasize;
  SkipListElem *elems;
} SkipList;

typedef enum
{
  MEOS_SUCCESS                  = 0,  

  MEOS_ERR_INTERNAL_ERROR       = 1,  
  MEOS_ERR_INTERNAL_TYPE_ERROR  = 2,  
  MEOS_ERR_VALUE_OUT_OF_RANGE   = 3,  
  MEOS_ERR_DIVISION_BY_ZERO     = 4,  
  MEOS_ERR_MEMORY_ALLOC_ERROR   = 5,  
  MEOS_ERR_AGGREGATION_ERROR    = 6,  
  MEOS_ERR_DIRECTORY_ERROR      = 7,  
  MEOS_ERR_FILE_ERROR           = 8,  

  MEOS_ERR_INVALID_ARG          = 10, 
  MEOS_ERR_INVALID_ARG_TYPE     = 11, 
  MEOS_ERR_INVALID_ARG_VALUE    = 12, 

  MEOS_ERR_MFJSON_INPUT         = 20, 
  MEOS_ERR_MFJSON_OUTPUT        = 21, 
  MEOS_ERR_TEXT_INPUT           = 22, 
  MEOS_ERR_TEXT_OUTPUT          = 23, 
  MEOS_ERR_WKB_INPUT            = 24, 
  MEOS_ERR_WKB_OUTPUT           = 25, 
  MEOS_ERR_GEOJSON_INPUT        = 26, 
  MEOS_ERR_GEOJSON_OUTPUT       = 27, 

} errorCode;


extern int meos_errno(void);
extern int meos_errno_set(int err);
extern int meos_errno_restore(int err);
extern int meos_errno_reset(void);

extern void meos_initialize_timezone(const char *name);
extern void meos_finalize_timezone(void);

extern void meos_initialize(const char *tz_str);
extern void meos_finalize(void);

extern bool bool_in(const char *in_str);
extern char *bool_out(bool b);
extern text *cstring2text(const char *cstring);
extern DateADT pg_date_in(const char *str);
extern char *pg_date_out(DateADT date);
extern int pg_interval_cmp(const Interval *interval1, const Interval *interval2);
extern Interval *pg_interval_in(const char *str, int32 typmod);
extern Interval *pg_interval_make(int32 years, int32 months, int32 weeks, int32 days, int32 hours, int32 mins, double secs);
extern Interval *pg_interval_mul(const Interval *span, double factor);
extern char *pg_interval_out(const Interval *span);
extern text *pg_interval_to_char(Interval *it, text *fmt);
extern Interval *pg_interval_pl(const Interval *span1, const Interval *span2);
extern TimeADT pg_time_in(const char *str, int32 typmod);
extern char *pg_time_out(TimeADT time);
extern Timestamp pg_timestamp_in(const char *str, int32 typmod);
extern Interval *pg_timestamp_mi(TimestampTz dt1, TimestampTz dt2);
extern TimestampTz pg_timestamp_mi_interval(TimestampTz timestamp, const Interval *span);
extern char *pg_timestamp_out(Timestamp dt);
extern TimestampTz pg_timestamp_pl_interval(TimestampTz timestamp, const Interval *span);
extern text *pg_timestamp_to_char(Timestamp dt, text *fmt);
extern TimestampTz pg_timestamptz_in(const char *str, int32 typmod);
extern char *pg_timestamptz_out(TimestampTz dt);
extern text *pg_timestamptz_to_char(TimestampTz dt, text *fmt);
extern DateADT pg_to_date(text *date_txt, text *fmt);
extern TimestampTz pg_to_timestamp(text *date_txt, text *fmt);
extern char *text2cstring(const text *textptr);

extern GSERIALIZED *geography_from_hexewkb(const char *wkt);
extern GSERIALIZED *geography_from_text(char *wkt, int srid);
extern GSERIALIZED *geometry_from_hexewkb(const char *wkt);
extern GSERIALIZED *geometry_from_text(char *wkt, int srid);
extern bytea *gserialized_as_ewkb(const GSERIALIZED *gs, char *type);
extern char *gserialized_as_ewkt(const GSERIALIZED *gs, int precision);
extern char *gserialized_as_geojson(const GSERIALIZED *gs, int option, int precision, char *srs);
extern char *gserialized_as_hexewkb(const GSERIALIZED *gs, const char *type);
extern char *gserialized_as_text(const GSERIALIZED *gs, int precision);
extern GSERIALIZED *gserialized_from_ewkb(const bytea *bytea_wkb, int32 srid);
extern GSERIALIZED *gserialized_from_geojson(const char *geojson);
extern char *gserialized_out(const GSERIALIZED *gs);
extern GSERIALIZED *pgis_geography_in(char *input, int32 geom_typmod);
extern GSERIALIZED *pgis_geometry_in(char *input, int32 geom_typmod);
extern bool pgis_gserialized_same(const GSERIALIZED *gs1, const GSERIALIZED *gs2);

extern Set *bigintset_in(const char *str);
extern char *bigintset_out(const Set *set);
extern Span *bigintspan_in(const char *str);
extern char *bigintspan_out(const Span *s);
extern SpanSet *bigintspanset_in(const char *str);
extern char *bigintspanset_out(const SpanSet *ss);
extern Set *floatset_in(const char *str);
extern char *floatset_out(const Set *set, int maxdd);
extern Span *floatspan_in(const char *str);
extern char *floatspan_out(const Span *s, int maxdd);
extern SpanSet *floatspanset_in(const char *str);
extern char *floatspanset_out(const SpanSet *ss, int maxdd);
extern Set *geogset_in(const char *str);
extern Set *geomset_in(const char *str);
extern char *geoset_as_ewkt(const Set *set, int maxdd);
extern char *geoset_as_text(const Set *set, int maxdd);
extern char *geoset_out(const Set *set, int maxdd);
extern Set *intset_in(const char *str);
extern char *intset_out(const Set *set);
extern Span *intspan_in(const char *str);
extern char *intspan_out(const Span *s);
extern SpanSet *intspanset_in(const char *str);
extern char *intspanset_out(const SpanSet *ss);
extern Span *period_in(const char *str);
extern char *period_out(const Span *s);
extern SpanSet *periodset_in(const char *str);
extern char *periodset_out(const SpanSet *ss);
extern char *set_as_hexwkb(const Set *s, uint8_t variant, size_t *size_out);
extern uint8_t *set_as_wkb(const Set *s, uint8_t variant, size_t *size_out);
extern Set *set_from_hexwkb(const char *hexwkb);
extern Set *set_from_wkb(const uint8_t *wkb, size_t size);
extern char *span_as_hexwkb(const Span *s, uint8_t variant, size_t *size_out);
extern uint8_t *span_as_wkb(const Span *s, uint8_t variant, size_t *size_out);
extern Span *span_from_hexwkb(const char *hexwkb);
extern Span *span_from_wkb(const uint8_t *wkb, size_t size);
extern char *spanset_as_hexwkb(const SpanSet *ss, uint8_t variant, size_t *size_out);
extern uint8_t *spanset_as_wkb(const SpanSet *ss, uint8_t variant, size_t *size_out);
extern SpanSet *spanset_from_hexwkb(const char *hexwkb);
extern SpanSet *spanset_from_wkb(const uint8_t *wkb, size_t size);
extern Set *textset_in(const char *str);
extern char *textset_out(const Set *set);
extern Set *timestampset_in(const char *str);
extern char *timestampset_out(const Set *set);

extern Set *bigintset_make(const int64 *values, int count);
extern Span *bigintspan_make(int64 lower, int64 upper, bool lower_inc, bool upper_inc);
extern Set *floatset_make(const double *values, int count);
extern Span *floatspan_make(double lower, double upper, bool lower_inc, bool upper_inc);
extern Set *geoset_make(const GSERIALIZED **values, int count);
extern Set *intset_make(const int *values, int count);
extern Span *intspan_make(int lower, int upper, bool lower_inc, bool upper_inc);
extern Span *period_make(TimestampTz lower, TimestampTz upper, bool lower_inc, bool upper_inc);
extern Set *set_copy(const Set *s);
extern Span *span_copy(const Span *s);
extern SpanSet *spanset_copy(const SpanSet *ps);
extern SpanSet *spanset_make(Span *spans, int count, bool normalize);
extern Set *textset_make(const text **values, int count);
extern Set *timestampset_make(const TimestampTz *values, int count);

extern Set *bigint_to_bigintset(int64 i);
extern Span *bigint_to_bigintspan(int i);
extern SpanSet *bigint_to_bigintspanset(int i);
extern Set *float_to_floatset(double d);
extern Span *float_to_floatspan(double d);
extern SpanSet *float_to_floatspanset(double d);
extern Set *geo_to_geoset(GSERIALIZED *gs);
extern Set *int_to_intset(int i);
extern Span *int_to_intspan(int i);
extern SpanSet *int_to_intspanset(int i);
extern SpanSet *set_to_spanset(const Set *s);
extern SpanSet *span_to_spanset(const Span *s);
extern Set *text_to_textset(text *txt);
extern Span *timestamp_to_period(TimestampTz t);
extern SpanSet *timestamp_to_periodset(TimestampTz t);
extern Set *timestamp_to_tstzset(TimestampTz t);

extern int64 bigintset_end_value(const Set *s);
extern int64 bigintset_start_value(const Set *s);
extern bool bigintset_value_n(const Set *s, int n, int64 *result);
extern int64 *bigintset_values(const Set *s);
extern int bigintspan_lower(const Span *s);
extern int bigintspan_upper(const Span *s);
extern int bigintspanset_lower(const SpanSet *ss);
extern int bigintspanset_upper(const SpanSet *ss);
extern double floatset_end_value(const Set *s);
extern double floatset_start_value(const Set *s);
extern bool floatset_value_n(const Set *s, int n, double *result);
extern double *floatset_values(const Set *s);
extern double floatspan_lower(const Span *s);
extern double floatspan_upper(const Span *s);
extern double floatspanset_lower(const SpanSet *ss);
extern double floatspanset_upper(const SpanSet *ss);
extern GSERIALIZED *geoset_end_value(const Set *s);
extern int geoset_srid(const Set *set);
extern GSERIALIZED *geoset_start_value(const Set *s);
extern bool geoset_value_n(const Set *s, int n, GSERIALIZED **result);
extern GSERIALIZED **geoset_values(const Set *s);
extern int intset_end_value(const Set *s);
extern int intset_start_value(const Set *s);
extern bool intset_value_n(const Set *s, int n, int *result);
extern int *intset_values(const Set *s);
extern int intspan_lower(const Span *s);
extern int intspan_upper(const Span *s);
extern int intspanset_lower(const SpanSet *ss);
extern int intspanset_upper(const SpanSet *ss);
extern Interval *period_duration(const Span *s);
extern TimestampTz period_lower(const Span *p);
extern TimestampTz period_upper(const Span *p);
extern Interval *periodset_duration(const SpanSet *ps, bool boundspan);
extern TimestampTz periodset_end_timestamp(const SpanSet *ps);
extern TimestampTz periodset_lower(const SpanSet *ps);
extern int periodset_num_timestamps(const SpanSet *ps);
extern TimestampTz periodset_start_timestamp(const SpanSet *ps);
extern bool periodset_timestamp_n(const SpanSet *ps, int n, TimestampTz *result);
extern TimestampTz *periodset_timestamps(const SpanSet *ps, int *count);
extern TimestampTz periodset_upper(const SpanSet *ps);
extern uint64 set_hash(const Set *s);
extern uint64 set_hash_extended(const Set *s, uint64 seed);
extern int set_num_values(const Set *s);
extern Span *set_span(const Set *s);
extern uint64 span_hash(const Span *s);
extern uint64 span_hash_extended(const Span *s, uint64 seed);
extern bool span_lower_inc(const Span *s);
extern bool span_upper_inc(const Span *s);
extern double span_width(const Span *s);
extern Span *spanset_end_span(const SpanSet *ss);
extern uint64 spanset_hash(const SpanSet *ps);
extern uint64 spanset_hash_extended(const SpanSet *ps, uint64 seed);
extern bool spanset_lower_inc(const SpanSet *ss);
extern int spanset_num_spans(const SpanSet *ss);
extern Span *spanset_span(const SpanSet *ss);
extern Span *spanset_span_n(const SpanSet *ss, int i);
extern const Span **spanset_spans(const SpanSet *ss);
extern Span *spanset_start_span(const SpanSet *ss);
extern bool spanset_upper_inc(const SpanSet *ss);
extern double spanset_width(const SpanSet *ss, bool boundspan);
extern STBox *spatialset_stbox(const Set *s);
extern text *textset_end_value(const Set *s);
extern text *textset_start_value(const Set *s);
extern bool textset_value_n(const Set *s, int n, text **result);
extern text **textset_values(const Set *s);
extern TimestampTz timestampset_end_timestamp(const Set *ts);
extern TimestampTz timestampset_start_timestamp(const Set *ts);
extern bool timestampset_timestamp_n(const Set *ts, int n, TimestampTz *result);
extern TimestampTz *timestampset_values(const Set *ts);

extern Set *bigintset_shift_scale(const Set *s, int64 shift, int64 width, bool hasshift, bool haswidth);
extern Span *bigintspan_shift_scale(const Span *s, int64 shift, int64 width, bool hasshift, bool haswidth);
extern SpanSet *bigintspanset_shift_scale(const SpanSet *ss, int64 shift, int64 width, bool hasshift, bool haswidth);
extern Set *floatset_round(const Set *s, int maxdd);
extern Set *floatset_shift_scale(const Set *s, double shift, double width, bool hasshift, bool haswidth);
extern Span *floatspan_intspan(const Span *s);
extern Span *floatspan_round(const Span *s, int maxdd);
extern Span *floatspan_shift_scale(const Span *s, double shift, double width, bool hasshift, bool haswidth);
extern SpanSet *floatspanset_intspanset(const SpanSet *ss);
extern SpanSet *floatspanset_round(const SpanSet *ss, int maxdd);
extern SpanSet *floatspanset_shift_scale(const SpanSet *ss, double shift, double width, bool hasshift, bool haswidth);
extern Set *geoset_round(const Set *s, int maxdd);
extern Set *intset_shift_scale(const Set *s, int shift, int width, bool hasshift, bool haswidth);
extern Span *intspan_floatspan(const Span *s);
extern Span *intspan_shift_scale(const Span *s, int shift, int width, bool hasshift, bool haswidth);
extern SpanSet *intspanset_floatspanset(const SpanSet *ss);
extern SpanSet *intspanset_shift_scale(const SpanSet *ss, int shift, int width, bool hasshift, bool haswidth);
extern Span *period_shift_scale(const Span *p, const Interval *shift, const Interval *duration);
extern Span *period_tprecision(const Span *s, const Interval *duration, TimestampTz torigin);
extern SpanSet *periodset_shift_scale(const SpanSet *ss, const Interval *shift, const Interval *duration);
extern SpanSet *periodset_tprecision(const SpanSet *ss, const Interval *duration, TimestampTz torigin);
extern Set *textset_lower(const Set *s);
extern Set *textset_upper(const Set *s);
extern TimestampTz timestamp_tprecision(TimestampTz t, const Interval *duration, TimestampTz torigin);
extern Set *timestampset_shift_scale(const Set *ts, const Interval *shift, const Interval *duration);

extern bool intersection_bigintset_bigint(const Set *s, int64 i, int64 *result);
extern bool intersection_bigintspan_bigint(const Span *s, int64 i, int64 *result);
extern bool intersection_bigintspanset_bigint(const SpanSet *ss, int64 i, int64 *result);
extern bool intersection_floatset_float(const Set *s, double d, double *result);
extern bool intersection_floatspan_float(const Span *s, double d, double *result);
extern bool intersection_floatspanset_float(const SpanSet *ss, double d, double *result);
extern bool intersection_geoset_geo(const Set *s, const GSERIALIZED *gs, GSERIALIZED **result);
extern bool intersection_intset_int(const Set *s, int i, int *result);
extern bool intersection_intspan_int(const Span *s, int i, int *result);
extern bool intersection_intspanset_int(const SpanSet *ss, int i, int *result);
extern bool intersection_period_timestamp(const Span *s, TimestampTz t, TimestampTz *result);
extern bool intersection_periodset_timestamp(const SpanSet *ss, TimestampTz t, TimestampTz *result);
extern Set *intersection_set_set(const Set *s1, const Set *s2);
extern Span *intersection_span_span(const Span *s1, const Span *s2);
extern SpanSet *intersection_spanset_span(const SpanSet *ss, const Span *s);
extern SpanSet *intersection_spanset_spanset(const SpanSet *ss1, const SpanSet *ss2);
extern bool intersection_textset_text(const Set *s, const text *txt, text **result);
extern bool intersection_timestampset_timestamp(const Set *s, TimestampTz t, TimestampTz *result);
extern bool minus_bigint_bigintset(int64 i, const Set *s, int64 *result);
extern bool minus_bigint_bigintspan(int64 i, const Span *s, int64 *result);
extern bool minus_bigint_bigintspanset(int64 i, const SpanSet *ss, int64 *result);
extern Set *minus_bigintset_bigint(const Set *s, int64 i);
extern SpanSet *minus_bigintspan_bigint(const Span *s, int64 i);
extern SpanSet *minus_bigintspanset_bigint(const SpanSet *ss, int64 i);
extern bool minus_float_floatset(double d, const Set *s, double *result);
extern bool minus_float_floatspan(double d, const Span *s, double *result);
extern bool minus_float_floatspanset(double d, const SpanSet *ss, double *result);
extern Set *minus_floatset_float(const Set *s, double d);
extern SpanSet *minus_floatspan_float(const Span *s, double d);
extern SpanSet *minus_floatspanset_float(const SpanSet *ss, double d);
extern bool minus_geo_geoset(const GSERIALIZED *gs, const Set *s, GSERIALIZED **result);
extern Set *minus_geoset_geo(const Set *s, const GSERIALIZED *gs);
extern bool minus_int_intset(int i, const Set *s, int *result);
extern bool minus_int_intspan(int i, const Span *s, int *result);
extern bool minus_int_intspanset(int i, const SpanSet *ss, int *result);
extern Set *minus_intset_int(const Set *s, int i);
extern SpanSet *minus_intspan_int(const Span *s, int i);
extern SpanSet *minus_intspanset_int(const SpanSet *ss, int i);
extern SpanSet *minus_period_timestamp(const Span *s, TimestampTz t);
extern SpanSet *minus_periodset_timestamp(const SpanSet *ss, TimestampTz t);
extern Set *minus_set_set(const Set *s1, const Set *s2);
extern SpanSet *minus_span_span(const Span *s1, const Span *s2);
extern SpanSet *minus_span_spanset(const Span *s, const SpanSet *ss);
extern SpanSet *minus_spanset_span(const SpanSet *ss, const Span *s);
extern SpanSet *minus_spanset_spanset(const SpanSet *ss1, const SpanSet *ss2);
extern bool minus_text_textset(const text *txt, const Set *s, text **result);
extern Set *minus_textset_text(const Set *s, const text *txt);
extern bool minus_timestamp_period(TimestampTz t, const Span *s, TimestampTz *result);
extern bool minus_timestamp_periodset(TimestampTz t, const SpanSet *ss, TimestampTz *result);
extern bool minus_timestamp_timestampset(TimestampTz t, const Set *s, TimestampTz *result);
extern Set *minus_timestampset_timestamp(const Set *s, TimestampTz t);
extern Set *union_bigintset_bigint(const Set *s, int64 i);
extern SpanSet *union_bigintspan_bigint(const Span *s, int64 i);
extern SpanSet *union_bigintspanset_bigint(const SpanSet *ss, int64 i);
extern Set *union_floatset_float(const Set *s, double d);
extern SpanSet *union_floatspan_float(const Span *s, double d);
extern SpanSet *union_floatspanset_float(const SpanSet *ss, double d);
extern Set *union_geoset_geo(const Set *s, const GSERIALIZED *gs);
extern Set *union_intset_int(const Set *s, int i);
extern SpanSet *union_intspan_int(const Span *s, int i);
extern SpanSet *union_intspanset_int(const SpanSet *ss, int i);
extern SpanSet *union_period_timestamp(const Span *s, TimestampTz t);
extern SpanSet *union_periodset_timestamp(SpanSet *ss, TimestampTz t);
extern Set *union_set_set(const Set *s1, const Set *s2);
extern SpanSet *union_span_span(const Span *s1, const Span *s2);
extern SpanSet *union_spanset_span(const SpanSet *ss, const Span *s);
extern SpanSet *union_spanset_spanset(const SpanSet *ss1, const SpanSet *ss2);
extern Set *union_textset_text(const Set *s, const text *txt);
extern Set *union_timestampset_timestamp(const Set *s, const TimestampTz t);

extern bool adjacent_bigintspan_bigint(const Span *s, int64 i);
extern bool adjacent_bigintspanset_bigint(const SpanSet *ss, int64 i);
extern bool adjacent_floatspan_float(const Span *s, double d);
extern bool adjacent_floatspanset_float(const SpanSet *ss, double d);
extern bool adjacent_intspan_int(const Span *s, int i);
extern bool adjacent_intspanset_int(const SpanSet *ss, int i);
extern bool adjacent_period_timestamp(const Span *p, TimestampTz t);
extern bool adjacent_periodset_timestamp(const SpanSet *ps, TimestampTz t);
extern bool adjacent_span_span(const Span *s1, const Span *s2);
extern bool adjacent_spanset_span(const SpanSet *ss, const Span *s);
extern bool adjacent_spanset_spanset(const SpanSet *ss1, const SpanSet *ss2);
extern bool contained_bigint_bigintset(int64 i, const Set *s);
extern bool contained_bigint_bigintspan(int64 i, const Span *s);
extern bool contained_bigint_bigintspanset(int64 i, const SpanSet *ss);
extern bool contained_float_floatset(double d, const Set *s);
extern bool contained_float_floatspan(double d, const Span *s);
extern bool contained_float_floatspanset(double d, const SpanSet *ss);
extern bool contained_int_intset(int i, const Set *s);
extern bool contained_int_intspan(int i, const Span *s);
extern bool contained_int_intspanset(int i, const SpanSet *ss);
extern bool contained_set_set(const Set *s1, const Set *s2);
extern bool contained_span_span(const Span *s1, const Span *s2);
extern bool contained_span_spanset(const Span *s, const SpanSet *ss);
extern bool contained_spanset_span(const SpanSet *ss, const Span *s);
extern bool contained_spanset_spanset(const SpanSet *ss1, const SpanSet *ss2);
extern bool contained_text_textset(text *txt, const Set *s);
extern bool contained_timestamp_period(TimestampTz t, const Span *p);
extern bool contained_timestamp_periodset(TimestampTz t, const SpanSet *ss);
extern bool contained_timestamp_timestampset(TimestampTz t, const Set *ts);
extern bool contains_bigintset_bigint(const Set *s, int64 i);
extern bool contains_bigintspan_bigint(const Span *s, int64 i);
extern bool contains_bigintspanset_bigint(const SpanSet *ss, int64 i);
extern bool contains_floatset_float(const Set *s, double d);
extern bool contains_floatspan_float(const Span *s, double d);
extern bool contains_floatspanset_float(const SpanSet *ss, double d);
extern bool contains_intset_int(const Set *s, int i);
extern bool contains_intspan_int(const Span *s, int i);
extern bool contains_intspanset_int(const SpanSet *ss, int i);
extern bool contains_period_timestamp(const Span *p, TimestampTz t);
extern bool contains_periodset_timestamp(const SpanSet *ps, TimestampTz t);
extern bool contains_set_set(const Set *s1, const Set *s2);
extern bool contains_span_span(const Span *s1, const Span *s2);
extern bool contains_span_spanset(const Span *s, const SpanSet *ss);
extern bool contains_spanset_span(const SpanSet *ss, const Span *s);
extern bool contains_spanset_spanset(const SpanSet *ss1, const SpanSet *ss2);
extern bool contains_textset_text(const Set *s, text *t);
extern bool contains_timestampset_timestamp(const Set *s, TimestampTz t);
extern bool overlaps_set_set(const Set *s1, const Set *s2);
extern bool overlaps_span_span(const Span *s1, const Span *s2);
extern bool overlaps_spanset_span(const SpanSet *ss, const Span *s);
extern bool overlaps_spanset_spanset(const SpanSet *ss1, const SpanSet *ss2);

extern bool after_period_timestamp(const Span *s, TimestampTz t);
extern bool after_periodset_timestamp(const SpanSet *ss, TimestampTz t);
extern bool after_timestamp_period(TimestampTz t, const Span *s);
extern bool after_timestamp_periodset(TimestampTz t, const SpanSet *ss);
extern bool after_timestamp_timestampset(TimestampTz t, const Set *ts);
extern bool after_timestampset_timestamp(const Set *s, TimestampTz t);
extern bool before_period_timestamp(const Span *s, TimestampTz t);
extern bool before_periodset_timestamp(const SpanSet *ss, TimestampTz t);
extern bool before_timestamp_period(TimestampTz t, const Span *s);
extern bool before_timestamp_periodset(TimestampTz t, const SpanSet *ss);
extern bool before_timestamp_timestampset(TimestampTz t, const Set *ts);
extern bool before_timestampset_timestamp(const Set *s, TimestampTz t);
extern bool left_bigint_bigintset(int64 i, const Set *s);
extern bool left_bigint_bigintspan(int64 i, const Span *s);
extern bool left_bigint_bigintspanset(int64 i, const SpanSet *ss);
extern bool left_bigintset_bigint(const Set *s, int64 i);
extern bool left_bigintspan_bigint(const Span *s, int64 i);
extern bool left_bigintspanset_bigint(const SpanSet *ss, int64 i);
extern bool left_float_floatset(double d, const Set *s);
extern bool left_float_floatspan(double d, const Span *s);
extern bool left_float_floatspanset(double d, const SpanSet *ss);
extern bool left_floatset_float(const Set *s, double d);
extern bool left_floatspan_float(const Span *s, double d);
extern bool left_floatspanset_float(const SpanSet *ss, double d);
extern bool left_int_intset(int i, const Set *s);
extern bool left_int_intspan(int i, const Span *s);
extern bool left_int_intspanset(int i, const SpanSet *ss);
extern bool left_intset_int(const Set *s, int i);
extern bool left_intspan_int(const Span *s, int i);
extern bool left_intspanset_int(const SpanSet *ss, int i);
extern bool left_set_set(const Set *s1, const Set *s2);
extern bool left_span_span(const Span *s1, const Span *s2);
extern bool left_span_spanset(const Span *s, const SpanSet *ss);
extern bool left_spanset_span(const SpanSet *ss, const Span *s);
extern bool left_spanset_spanset(const SpanSet *ss1, const SpanSet *ss2);
extern bool left_text_textset(text *txt, const Set *s);
extern bool left_textset_text(const Set *s, text *txt);
extern bool overafter_period_timestamp(const Span *s, TimestampTz t);
extern bool overafter_periodset_timestamp(const SpanSet *ss, TimestampTz t);
extern bool overafter_timestamp_period(TimestampTz t, const Span *s);
extern bool overafter_timestamp_periodset(TimestampTz t, const SpanSet *ss);
extern bool overafter_timestamp_timestampset(TimestampTz t, const Set *ts);
extern bool overafter_timestampset_timestamp(const Set *s, TimestampTz t);
extern bool overbefore_period_timestamp(const Span *s, TimestampTz t);
extern bool overbefore_periodset_timestamp(const SpanSet *ss, TimestampTz t);
extern bool overbefore_timestamp_period(TimestampTz t, const Span *s);
extern bool overbefore_timestamp_periodset(TimestampTz t, const SpanSet *ss);
extern bool overbefore_timestamp_timestampset(TimestampTz t, const Set *ts);
extern bool overbefore_timestampset_timestamp(const Set *s, TimestampTz t);
extern bool overleft_bigint_bigintset(int64 i, const Set *s);
extern bool overleft_bigint_bigintspan(int64 i, const Span *s);
extern bool overleft_bigint_bigintspanset(int64 i, const SpanSet *ss);
extern bool overleft_bigintset_bigint(const Set *s, int64 i);
extern bool overleft_bigintspan_bigint(const Span *s, int64 i);
extern bool overleft_bigintspanset_bigint(const SpanSet *ss, int64 i);
extern bool overleft_float_floatset(double d, const Set *s);
extern bool overleft_float_floatspan(double d, const Span *s);
extern bool overleft_float_floatspanset(double d, const SpanSet *ss);
extern bool overleft_floatset_float(const Set *s, double d);
extern bool overleft_floatspan_float(const Span *s, double d);
extern bool overleft_floatspanset_float(const SpanSet *ss, double d);
extern bool overleft_int_intset(int i, const Set *s);
extern bool overleft_int_intspan(int i, const Span *s);
extern bool overleft_int_intspanset(int i, const SpanSet *ss);
extern bool overleft_intset_int(const Set *s, int i);
extern bool overleft_intspan_int(const Span *s, int i);
extern bool overleft_intspanset_int(const SpanSet *ss, int i);
extern bool overleft_set_set(const Set *s1, const Set *s2);
extern bool overleft_span_span(const Span *s1, const Span *s2);
extern bool overleft_span_spanset(const Span *s, const SpanSet *ss);
extern bool overleft_spanset_span(const SpanSet *ss, const Span *s);
extern bool overleft_spanset_spanset(const SpanSet *ss1, const SpanSet *ss2);
extern bool overleft_text_textset(text *txt, const Set *s);
extern bool overleft_textset_text(const Set *s, text *txt);
extern bool overright_bigint_bigintset(int64 i, const Set *s);
extern bool overright_bigint_bigintspan(int64 i, const Span *s);
extern bool overright_bigint_bigintspanset(int64 i, const SpanSet *ss);
extern bool overright_bigintset_bigint(const Set *s, int64 i);
extern bool overright_bigintspan_bigint(const Span *s, int64 i);
extern bool overright_bigintspanset_bigint(const SpanSet *ss, int64 i);
extern bool overright_float_floatset(double d, const Set *s);
extern bool overright_float_floatspan(double d, const Span *s);
extern bool overright_float_floatspanset(double d, const SpanSet *ss);
extern bool overright_floatset_float(const Set *s, double d);
extern bool overright_floatspan_float(const Span *s, double d);
extern bool overright_floatspanset_float(const SpanSet *ss, double d);
extern bool overright_int_intset(int i, const Set *s);
extern bool overright_int_intspan(int i, const Span *s);
extern bool overright_int_intspanset(int i, const SpanSet *ss);
extern bool overright_intset_int(const Set *s, int i);
extern bool overright_intspan_int(const Span *s, int i);
extern bool overright_intspanset_int(const SpanSet *ss, int i);
extern bool overright_set_set(const Set *s1, const Set *s2);
extern bool overright_span_span(const Span *s1, const Span *s2);
extern bool overright_span_spanset(const Span *s, const SpanSet *ss);
extern bool overright_spanset_span(const SpanSet *ss, const Span *s);
extern bool overright_spanset_spanset(const SpanSet *ss1, const SpanSet *ss2);
extern bool overright_text_textset(text *txt, const Set *s);
extern bool overright_textset_text(const Set *s, text *txt);
extern bool right_bigint_bigintset(int64 i, const Set *s);
extern bool right_bigint_bigintspan(int64 i, const Span *s);
extern bool right_bigint_bigintspanset(int64 i, const SpanSet *ss);
extern bool right_bigintset_bigint(const Set *s, int64 i);
extern bool right_bigintspan_bigint(const Span *s, int64 i);
extern bool right_bigintspanset_bigint(const SpanSet *ss, int64 i);
extern bool right_float_floatset(double d, const Set *s);
extern bool right_float_floatspan(double d, const Span *s);
extern bool right_float_floatspanset(double d, const SpanSet *ss);
extern bool right_floatset_float(const Set *s, double d);
extern bool right_floatspan_float(const Span *s, double d);
extern bool right_floatspanset_float(const SpanSet *ss, double d);
extern bool right_int_intset(int i, const Set *s);
extern bool right_int_intspan(int i, const Span *s);
extern bool right_int_intspanset(int i, const SpanSet *ss);
extern bool right_intset_int(const Set *s, int i);
extern bool right_intspan_int(const Span *s, int i);
extern bool right_intspanset_int(const SpanSet *ss, int i);
extern bool right_set_set(const Set *s1, const Set *s2);
extern bool right_span_span(const Span *s1, const Span *s2);
extern bool right_span_spanset(const Span *s, const SpanSet *ss);
extern bool right_spanset_span(const SpanSet *ss, const Span *s);
extern bool right_spanset_spanset(const SpanSet *ss1, const SpanSet *ss2);
extern bool right_text_textset(text *txt, const Set *s);
extern bool right_textset_text(const Set *s, text *txt);

extern double distance_bigintset_bigint(const Set *s, int64 i);
extern double distance_bigintspan_bigint(const Span *s, int64 i);
extern double distance_bigintspanset_bigint(const SpanSet *ss, int64 i);
extern double distance_floatset_float(const Set *s, double d);
extern double distance_floatspan_float(const Span *s, double d);
extern double distance_floatspanset_float(const SpanSet *ss, double d);
extern double distance_intset_int(const Set *s, int i);
extern double distance_intspan_int(const Span *s, int i);
extern double distance_intspanset_int(const SpanSet *ss, int i);
extern double distance_period_timestamp(const Span *s, TimestampTz t);
extern double distance_periodset_timestamp(const SpanSet *ss, TimestampTz t);
extern double distance_set_set(const Set *s1, const Set *s2);
extern double distance_span_span(const Span *s1, const Span *s2);
extern double distance_spanset_span(const SpanSet *ss, const Span *s);
extern double distance_spanset_spanset(const SpanSet *ss1, const SpanSet *ss2);
extern double distance_timestampset_timestamp(const Set *s, TimestampTz t);

extern int set_cmp(const Set *s1, const Set *s2);
extern bool set_eq(const Set *s1, const Set *s2);
extern bool set_ge(const Set *s1, const Set *s2);
extern bool set_gt(const Set *s1, const Set *s2);
extern bool set_le(const Set *s1, const Set *s2);
extern bool set_lt(const Set *s1, const Set *s2);
extern bool set_ne(const Set *s1, const Set *s2);
extern int span_cmp(const Span *s1, const Span *s2);
extern bool span_eq(const Span *s1, const Span *s2);
extern bool span_ge(const Span *s1, const Span *s2);
extern bool span_gt(const Span *s1, const Span *s2);
extern bool span_le(const Span *s1, const Span *s2);
extern bool span_lt(const Span *s1, const Span *s2);
extern bool span_ne(const Span *s1, const Span *s2);
extern int spanset_cmp(const SpanSet *ss1, const SpanSet *ss2);
extern bool spanset_eq(const SpanSet *ss1, const SpanSet *ss2);
extern bool spanset_ge(const SpanSet *ss1, const SpanSet *ss2);
extern bool spanset_gt(const SpanSet *ss1, const SpanSet *ss2);
extern bool spanset_le(const SpanSet *ss1, const SpanSet *ss2);
extern bool spanset_lt(const SpanSet *ss1, const SpanSet *ss2);
extern bool spanset_ne(const SpanSet *ss1, const SpanSet *ss2);

extern Span *bigint_extent_transfn(Span *s, int64 i);
extern Set *bigint_union_transfn(Set *state, int64 i);
extern Span *float_extent_transfn(Span *s, double d);
extern Set *float_union_transfn(Set *state, double d);
extern Span *int_extent_transfn(Span *s, int i);
extern Set *int_union_transfn(Set *state, int i);
extern SkipList *period_tcount_transfn(SkipList *state, const Span *p);
extern SkipList *periodset_tcount_transfn(SkipList *state, const SpanSet *ps);
extern Span *set_extent_transfn(Span *span, const Set *set);
extern Set *set_union_finalfn(Set *state);
extern Set *set_union_transfn(Set *state, Set *set);
extern Span *span_extent_transfn(Span *s1, const Span *s2);
extern SpanSet *span_union_transfn(SpanSet *state, const Span *span);
extern Span *spanset_extent_transfn(Span *s, const SpanSet *ss);
extern SpanSet *spanset_union_finalfn(SpanSet *state);
extern SpanSet *spanset_union_transfn(SpanSet *state, const SpanSet *ss);
extern Set *text_union_transfn(Set *state, const text *txt);
extern Span *timestamp_extent_transfn(Span *p, TimestampTz t);
extern SkipList *timestamp_tcount_transfn(SkipList *state, TimestampTz t);
extern Set *timestamp_union_transfn(Set *state, TimestampTz t);
extern SkipList *timestampset_tcount_transfn(SkipList *state, const Set *ts);

extern TBox *tbox_in(const char *str);
extern char *tbox_out(const TBox *box, int maxdd);
extern TBox *tbox_from_wkb(const uint8_t *wkb, size_t size);
extern TBox *tbox_from_hexwkb(const char *hexwkb);
extern STBox *stbox_from_wkb(const uint8_t *wkb, size_t size);
extern STBox *stbox_from_hexwkb(const char *hexwkb);
extern uint8_t *tbox_as_wkb(const TBox *box, uint8_t variant, size_t *size_out);
extern char *tbox_as_hexwkb(const TBox *box, uint8_t variant, size_t *size);
extern uint8_t *stbox_as_wkb(const STBox *box, uint8_t variant, size_t *size_out);
extern char *stbox_as_hexwkb(const STBox *box, uint8_t variant, size_t *size);
extern STBox *stbox_in(const char *str);
extern char *stbox_out(const STBox *box, int maxdd);

extern TBox *float_period_to_tbox(double d, const Span *p);
extern TBox *float_timestamp_to_tbox(double d, TimestampTz t);
extern STBox *geo_period_to_stbox(const GSERIALIZED *gs, const Span *p);
extern STBox *geo_timestamp_to_stbox(const GSERIALIZED *gs, TimestampTz t);
extern TBox *int_period_to_tbox(int i, const Span *p);
extern TBox *int_timestamp_to_tbox(int i, TimestampTz t);
extern TBox *span_period_to_tbox(const Span *span, const Span *p);
extern TBox *span_timestamp_to_tbox(const Span *span, TimestampTz t);
extern STBox *stbox_copy(const STBox *box);
extern STBox *stbox_make(bool hasx, bool hasz, bool geodetic, int32 srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, const Span *p);
extern TBox *tbox_copy(const TBox *box);
extern TBox *tbox_make(const Span *s, const Span *p);

extern TBox *float_to_tbox(double d);
extern STBox *geo_to_stbox(const GSERIALIZED *gs);
extern TBox *int_to_tbox(int i);
extern TBox *numset_to_tbox(const Set *s);
extern TBox *numspan_to_tbox(const Span *s);
extern TBox *numspanset_to_tbox(const SpanSet *ss);
extern STBox *period_to_stbox(const Span *p);
extern TBox *period_to_tbox(const Span *p);
extern STBox *periodset_to_stbox(const SpanSet *ps);
extern TBox *periodset_to_tbox(const SpanSet *ps);
extern GSERIALIZED *stbox_to_geo(const STBox *box);
extern Span *stbox_to_period(const STBox *box);
extern Span *tbox_to_floatspan(const TBox *box);
extern Span *tbox_to_period(const TBox *box);
extern STBox *timestamp_to_stbox(TimestampTz t);
extern TBox *timestamp_to_tbox(TimestampTz t);
extern STBox *timestampset_to_stbox(const Set *ts);
extern TBox *timestampset_to_tbox(const Set *ss);
extern TBox *tnumber_to_tbox(const Temporal *temp);
extern STBox *tpoint_to_stbox(const Temporal *temp);

extern bool stbox_hast(const STBox *box);
extern bool stbox_hasx(const STBox *box);
extern bool stbox_hasz(const STBox *box);
extern bool stbox_isgeodetic(const STBox *box);
extern int32 stbox_srid(const STBox *box);
extern bool stbox_tmax(const STBox *box, TimestampTz *result);
extern bool stbox_tmax_inc(const STBox *box, bool *result);
extern bool stbox_tmin(const STBox *box, TimestampTz *result);
extern bool stbox_tmin_inc(const STBox *box, bool *result);
extern bool stbox_xmax(const STBox *box, double *result);
extern bool stbox_xmin(const STBox *box, double *result);
extern bool stbox_ymax(const STBox *box, double *result);
extern bool stbox_ymin(const STBox *box, double *result);
extern bool stbox_zmax(const STBox *box, double *result);
extern bool stbox_zmin(const STBox *box, double *result);
extern bool tbox_hast(const TBox *box);
extern bool tbox_hasx(const TBox *box);
extern bool tbox_tmax(const TBox *box, TimestampTz *result);
extern bool tbox_tmax_inc(const TBox *box, bool *result);
extern bool tbox_tmin(const TBox *box, TimestampTz *result);
extern bool tbox_tmin_inc(const TBox *box, bool *result);
extern bool tbox_xmax(const TBox *box, double *result);
extern bool tbox_xmax_inc(const TBox *box, bool *result);
extern bool tbox_xmin(const TBox *box, double *result);
extern bool tbox_xmin_inc(const TBox *box, bool *result);

extern STBox *stbox_expand_space(const STBox *box, double d);
extern STBox *stbox_expand_time(const STBox *box, const Interval *interval);
extern STBox *stbox_get_space(const STBox *box);
extern STBox *stbox_round(const STBox *box, int maxdd);
extern STBox *stbox_set_srid(const STBox *box, int32 srid);
extern STBox *stbox_shift_scale_time(const STBox *box, const Interval *shift, const Interval *duration);
extern TBox *tbox_expand_time(const TBox *box, const Interval *interval);
extern TBox *tbox_expand_value(const TBox *box, const double d);
extern TBox *tbox_round(const TBox *box, int maxdd);
extern TBox *tbox_shift_scale_float(const TBox *box, double shift, double width, bool hasshift, bool haswidth);
extern TBox *tbox_shift_scale_int(const TBox *box, int shift, int width, bool hasshift, bool haswidth);
extern TBox *tbox_shift_scale_time(const TBox *box, const Interval *shift, const Interval *duration);

extern TBox *union_tbox_tbox(const TBox *box1, const TBox *box2, bool strict);
extern bool inter_tbox_tbox(const TBox *box1, const TBox *box2, TBox *result);
extern TBox *intersection_tbox_tbox(const TBox *box1, const TBox *box2);
extern STBox *union_stbox_stbox(const STBox *box1, const STBox *box2, bool strict);
extern bool inter_stbox_stbox(const STBox *box1, const STBox *box2, STBox *result);
extern STBox *intersection_stbox_stbox(const STBox *box1, const STBox *box2);

extern bool contains_tbox_tbox(const TBox *box1, const TBox *box2);
extern bool contained_tbox_tbox(const TBox *box1, const TBox *box2);
extern bool overlaps_tbox_tbox(const TBox *box1, const TBox *box2);
extern bool same_tbox_tbox(const TBox *box1, const TBox *box2);
extern bool adjacent_tbox_tbox(const TBox *box1, const TBox *box2);
extern bool contains_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool contained_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool overlaps_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool same_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool adjacent_stbox_stbox(const STBox *box1, const STBox *box2);

extern bool left_tbox_tbox(const TBox *box1, const TBox *box2);
extern bool overleft_tbox_tbox(const TBox *box1, const TBox *box2);
extern bool right_tbox_tbox(const TBox *box1, const TBox *box2);
extern bool overright_tbox_tbox(const TBox *box1, const TBox *box2);
extern bool before_tbox_tbox(const TBox *box1, const TBox *box2);
extern bool overbefore_tbox_tbox(const TBox *box1, const TBox *box2);
extern bool after_tbox_tbox(const TBox *box1, const TBox *box2);
extern bool overafter_tbox_tbox(const TBox *box1, const TBox *box2);
extern bool left_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool overleft_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool right_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool overright_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool below_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool overbelow_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool above_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool overabove_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool front_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool overfront_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool back_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool overback_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool before_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool overbefore_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool after_stbox_stbox(const STBox *box1, const STBox *box2);
extern bool overafter_stbox_stbox(const STBox *box1, const STBox *box2);

extern STBox *stbox_quad_split(const STBox *box, int *count);

extern bool tbox_eq(const TBox *box1, const TBox *box2);
extern bool tbox_ne(const TBox *box1, const TBox *box2);
extern int tbox_cmp(const TBox *box1, const TBox *box2);
extern bool tbox_lt(const TBox *box1, const TBox *box2);
extern bool tbox_le(const TBox *box1, const TBox *box2);
extern bool tbox_ge(const TBox *box1, const TBox *box2);
extern bool tbox_gt(const TBox *box1, const TBox *box2);
extern bool stbox_eq(const STBox *box1, const STBox *box2);
extern bool stbox_ne(const STBox *box1, const STBox *box2);
extern int stbox_cmp(const STBox *box1, const STBox *box2);
extern bool stbox_lt(const STBox *box1, const STBox *box2);
extern bool stbox_le(const STBox *box1, const STBox *box2);
extern bool stbox_ge(const STBox *box1, const STBox *box2);
extern bool stbox_gt(const STBox *box1, const STBox *box2);

extern Temporal *tbool_in(const char *str);
extern char *tbool_out(const Temporal *temp);
extern char *temporal_as_hexwkb(const Temporal *temp, uint8_t variant, size_t *size_out);
extern char *temporal_as_mfjson(const Temporal *temp, bool with_bbox, int flags, int precision, char *srs);
extern uint8_t *temporal_as_wkb(const Temporal *temp, uint8_t variant, size_t *size_out);
extern Temporal *temporal_from_hexwkb(const char *hexwkb);
extern Temporal *temporal_from_mfjson(const char *mfjson);
extern Temporal *temporal_from_wkb(const uint8_t *wkb, size_t size);
extern Temporal *tfloat_in(const char *str);
extern char *tfloat_out(const Temporal *temp, int maxdd);
extern Temporal *tgeogpoint_in(const char *str);
extern Temporal *tgeompoint_in(const char *str);
extern Temporal *tint_in(const char *str);
extern char *tint_out(const Temporal *temp);
extern char *tpoint_as_ewkt(const Temporal *temp, int maxdd);
extern char *tpoint_as_text(const Temporal *temp, int maxdd);
extern char *tpoint_out(const Temporal *temp, int maxdd);
extern Temporal *ttext_in(const char *str);
extern char *ttext_out(const Temporal *temp);

extern Temporal *tbool_from_base_temp(bool b, const Temporal *temp);
extern TInstant *tboolinst_make(bool b, TimestampTz t);
extern TSequence *tboolseq_from_base_period(bool b, const Span *p);
extern TSequence *tboolseq_from_base_timestampset(bool b, const Set *ts);
extern TSequenceSet *tboolseqset_from_base_periodset(bool b, const SpanSet *ps);
extern Temporal *temporal_copy(const Temporal *temp);
extern Temporal *tfloat_from_base_temp(double d, const Temporal *temp);
extern TInstant *tfloatinst_make(double d, TimestampTz t);
extern TSequence *tfloatseq_from_base_period(double d, const Span *p, interpType interp);
extern TSequence *tfloatseq_from_base_timestampset(double d, const Set *ts);
extern TSequenceSet *tfloatseqset_from_base_periodset(double d, const SpanSet *ps, interpType interp);
extern Temporal *tint_from_base_temp(int i, const Temporal *temp);
extern TInstant *tintinst_make(int i, TimestampTz t);
extern TSequence *tintseq_from_base_period(int i, const Span *p);
extern TSequence *tintseq_from_base_timestampset(int i, const Set *ts);
extern TSequenceSet *tintseqset_from_base_periodset(int i, const SpanSet *ps);
extern Temporal *tpoint_from_base_temp(const GSERIALIZED *gs, const Temporal *temp);
extern TInstant *tpointinst_make(const GSERIALIZED *gs, TimestampTz t);
extern TSequence *tpointseq_from_base_period(const GSERIALIZED *gs, const Span *p, interpType interp);
extern TSequence *tpointseq_from_base_timestampset(const GSERIALIZED *gs, const Set *ts);
extern TSequenceSet *tpointseqset_from_base_periodset(const GSERIALIZED *gs, const SpanSet *ps, interpType interp);
extern TSequence *tsequence_make(const TInstant **instants, int count, bool lower_inc, bool upper_inc, interpType interp, bool normalize);
extern TSequenceSet *tsequenceset_make(const TSequence **sequences, int count, bool normalize);
extern TSequenceSet *tsequenceset_make_gaps(const TInstant **instants, int count, interpType interp, Interval *maxt, double maxdist);
extern Temporal *ttext_from_base_temp(const text *txt, const Temporal *temp);
extern TInstant *ttextinst_make(const text *txt, TimestampTz t);
extern TSequence *ttextseq_from_base_period(const text *txt, const Span *p);
extern TSequence *ttextseq_from_base_timestampset(const text *txt, const Set *ts);
extern TSequenceSet *ttextseqset_from_base_periodset(const text *txt, const SpanSet *ps);

extern Span *temporal_to_period(const Temporal *temp);
extern Temporal *tfloat_to_tint(const Temporal *temp);
extern Temporal *tint_to_tfloat(const Temporal *temp);
extern Span *tnumber_to_span(const Temporal *temp);

extern bool tbool_end_value(const Temporal *temp);
extern bool tbool_start_value(const Temporal *temp);
extern bool *tbool_values(const Temporal *temp, int *count);
extern Interval *temporal_duration(const Temporal *temp, bool boundspan);
extern const TInstant *temporal_end_instant(const Temporal *temp);
extern TSequence *temporal_end_sequence(const Temporal *temp);
extern TimestampTz temporal_end_timestamp(const Temporal *temp);
extern uint64 temporal_hash(const Temporal *temp);
extern const TInstant *temporal_instant_n(const Temporal *temp, int n);
extern const TInstant **temporal_instants(const Temporal *temp, int *count);
extern char *temporal_interp(const Temporal *temp);
extern const TInstant *temporal_max_instant(const Temporal *temp);
extern const TInstant *temporal_min_instant(const Temporal *temp);
extern int temporal_num_instants(const Temporal *temp);
extern int temporal_num_sequences(const Temporal *temp);
extern int temporal_num_timestamps(const Temporal *temp);
extern TSequence **temporal_segments(const Temporal *temp, int *count);
extern TSequence *temporal_sequence_n(const Temporal *temp, int i);
extern TSequence **temporal_sequences(const Temporal *temp, int *count);
extern const TInstant *temporal_start_instant(const Temporal *temp);
extern TSequence *temporal_start_sequence(const Temporal *temp);
extern TimestampTz temporal_start_timestamp(const Temporal *temp);
extern TSequenceSet *temporal_stops(const Temporal *temp, double maxdist, const Interval *minduration);
extern char *temporal_subtype(const Temporal *temp);
extern SpanSet *temporal_time(const Temporal *temp);
extern bool temporal_timestamp_n(const Temporal *temp, int n, TimestampTz *result);
extern TimestampTz *temporal_timestamps(const Temporal *temp, int *count);
extern double tfloat_end_value(const Temporal *temp);
extern double tfloat_max_value(const Temporal *temp);
extern double tfloat_min_value(const Temporal *temp);
extern double tfloat_start_value(const Temporal *temp);
extern double *tfloat_values(const Temporal *temp, int *count);
extern int tint_end_value(const Temporal *temp);
extern int tint_max_value(const Temporal *temp);
extern int tint_min_value(const Temporal *temp);
extern int tint_start_value(const Temporal *temp);
extern int *tint_values(const Temporal *temp, int *count);
extern SpanSet *tnumber_valuespans(const Temporal *temp);
extern GSERIALIZED *tpoint_end_value(const Temporal *temp);
extern GSERIALIZED *tpoint_start_value(const Temporal *temp);
extern GSERIALIZED **tpoint_values(const Temporal *temp, int *count);
extern text *ttext_end_value(const Temporal *temp);
extern text *ttext_max_value(const Temporal *temp);
extern text *ttext_min_value(const Temporal *temp);
extern text *ttext_start_value(const Temporal *temp);
extern text **ttext_values(const Temporal *temp, int *count);

extern Temporal *temporal_scale_time(const Temporal *temp, const Interval *duration);
extern Temporal *temporal_set_interp(const Temporal *temp, interpType interp);
extern Temporal *temporal_shift_scale_time(const Temporal *temp, const Interval *shift, const Interval *duration);
extern Temporal *temporal_shift_time(const Temporal *temp, const Interval *shift);
extern Temporal *temporal_to_tinstant(const Temporal *temp);
extern Temporal *temporal_to_tsequence(const Temporal *temp, interpType interp);
extern Temporal *temporal_to_tsequenceset(const Temporal *temp, interpType interp);
extern Temporal *tfloat_scale_value(const Temporal *temp, double width);
extern Temporal *tfloat_shift_scale_value(const Temporal *temp, double shift, double width);
extern Temporal *tfloat_shift_value(const Temporal *temp, double shift);
extern Temporal *tint_scale_value(const Temporal *temp, int width);
extern Temporal *tint_shift_scale_value(const Temporal *temp, int shift, int width);
extern Temporal *tint_shift_value(const Temporal *temp, int shift);

extern Temporal *temporal_append_tinstant(Temporal *temp, const TInstant *inst, double maxdist, Interval *maxt, bool expand);
extern Temporal *temporal_append_tsequence(Temporal *temp, const TSequence *seq, bool expand);
extern Temporal *temporal_delete_period(const Temporal *temp, const Span *p, bool connect);
extern Temporal *temporal_delete_periodset(const Temporal *temp, const SpanSet *ps, bool connect);
extern Temporal *temporal_delete_timestamp(const Temporal *temp, TimestampTz t, bool connect);
extern Temporal *temporal_delete_timestampset(const Temporal *temp, const Set *ts, bool connect);
extern Temporal *temporal_insert(const Temporal *temp1, const Temporal *temp2, bool connect);
extern Temporal *temporal_merge(const Temporal *temp1, const Temporal *temp2);
extern Temporal *temporal_merge_array(Temporal **temparr, int count);
extern Temporal *temporal_update(const Temporal *temp1, const Temporal *temp2, bool connect);

extern Temporal *tbool_at_value(const Temporal *temp, bool b);
extern Temporal *tbool_minus_value(const Temporal *temp, bool b);
extern bool tbool_value_at_timestamp(const Temporal *temp, TimestampTz t, bool strict, bool *value);
extern Temporal *temporal_at_max(const Temporal *temp);
extern Temporal *temporal_at_min(const Temporal *temp);
extern Temporal *temporal_at_period(const Temporal *temp, const Span *p);
extern Temporal *temporal_at_periodset(const Temporal *temp, const SpanSet *ps);
extern Temporal *temporal_at_timestamp(const Temporal *temp, TimestampTz t);
extern Temporal *temporal_at_timestampset(const Temporal *temp, const Set *ts);
extern Temporal *temporal_at_values(const Temporal *temp, const Set *set);
extern Temporal *temporal_minus_max(const Temporal *temp);
extern Temporal *temporal_minus_min(const Temporal *temp);
extern Temporal *temporal_minus_period(const Temporal *temp, const Span *p);
extern Temporal *temporal_minus_periodset(const Temporal *temp, const SpanSet *ps);
extern Temporal *temporal_minus_timestamp(const Temporal *temp, TimestampTz t);
extern Temporal *temporal_minus_timestampset(const Temporal *temp, const Set *ts);
extern Temporal *temporal_minus_values(const Temporal *temp, const Set *set);
extern Temporal *tfloat_at_value(const Temporal *temp, double d);
extern Temporal *tfloat_minus_value(const Temporal *temp, double d);
extern bool tfloat_value_at_timestamp(const Temporal *temp, TimestampTz t, bool strict, double *value);
extern Temporal *tint_at_value(const Temporal *temp, int i);
extern Temporal *tint_minus_value(const Temporal *temp, int i);
extern bool tint_value_at_timestamp(const Temporal *temp, TimestampTz t, bool strict, int *value);
extern Temporal *tnumber_at_span(const Temporal *temp, const Span *span);
extern Temporal *tnumber_at_spanset(const Temporal *temp, const SpanSet *ss);
extern Temporal *tnumber_at_tbox(const Temporal *temp, const TBox *box);
extern Temporal *tnumber_minus_span(const Temporal *temp, const Span *span);
extern Temporal *tnumber_minus_spanset(const Temporal *temp, const SpanSet *ss);
extern Temporal *tnumber_minus_tbox(const Temporal *temp, const TBox *box);
extern Temporal *tpoint_at_geom_time(const Temporal *temp, const GSERIALIZED *gs, const Span *zspan, const Span *period);
extern Temporal *tpoint_at_stbox(const Temporal *temp, const STBox *box, bool border_inc);
extern Temporal *tpoint_at_value(const Temporal *temp, GSERIALIZED *gs);
extern Temporal *tpoint_minus_geom_time(const Temporal *temp, const GSERIALIZED *gs, const Span *zspan, const Span *period);
extern Temporal *tpoint_minus_stbox(const Temporal *temp, const STBox *box, bool border_inc);
extern Temporal *tpoint_minus_value(const Temporal *temp, GSERIALIZED *gs);
extern bool tpoint_value_at_timestamp(const Temporal *temp, TimestampTz t, bool strict, GSERIALIZED **value);
extern Temporal *ttext_at_value(const Temporal *temp, text *txt);
extern Temporal *ttext_minus_value(const Temporal *temp, text *txt);
extern bool ttext_value_at_timestamp(const Temporal *temp, TimestampTz t, bool strict, text **value);

extern int temporal_cmp(const Temporal *temp1, const Temporal *temp2);
extern bool temporal_eq(const Temporal *temp1, const Temporal *temp2);
extern bool temporal_ge(const Temporal *temp1, const Temporal *temp2);
extern bool temporal_gt(const Temporal *temp1, const Temporal *temp2);
extern bool temporal_le(const Temporal *temp1, const Temporal *temp2);
extern bool temporal_lt(const Temporal *temp1, const Temporal *temp2);
extern bool temporal_ne(const Temporal *temp1, const Temporal *temp2);

extern bool tbool_always_eq(const Temporal *temp, bool b);
extern bool tbool_ever_eq(const Temporal *temp, bool b);
extern bool tfloat_always_eq(const Temporal *temp, double d);
extern bool tfloat_always_le(const Temporal *temp, double d);
extern bool tfloat_always_lt(const Temporal *temp, double d);
extern bool tfloat_ever_eq(const Temporal *temp, double d);
extern bool tfloat_ever_le(const Temporal *temp, double d);
extern bool tfloat_ever_lt(const Temporal *temp, double d);
extern bool tint_always_eq(const Temporal *temp, int i);
extern bool tint_always_le(const Temporal *temp, int i);
extern bool tint_always_lt(const Temporal *temp, int i);
extern bool tint_ever_eq(const Temporal *temp, int i);
extern bool tint_ever_le(const Temporal *temp, int i);
extern bool tint_ever_lt(const Temporal *temp, int i);
extern bool tpoint_always_eq(const Temporal *temp, const GSERIALIZED *gs);;
extern bool tpoint_ever_eq(const Temporal *temp, const GSERIALIZED *gs);;
extern bool ttext_always_eq(const Temporal *temp, text *txt);
extern bool ttext_always_le(const Temporal *temp, text *txt);
extern bool ttext_always_lt(const Temporal *temp, text *txt);
extern bool ttext_ever_eq(const Temporal *temp, text *txt);
extern bool ttext_ever_le(const Temporal *temp, text *txt);
extern bool ttext_ever_lt(const Temporal *temp, text *txt);

extern Temporal *teq_bool_tbool(bool b, const Temporal *temp);
extern Temporal *teq_float_tfloat(double d, const Temporal *temp);
extern Temporal *teq_int_tint(int i, const Temporal *temp);
extern Temporal *teq_point_tpoint(const GSERIALIZED *gs, const Temporal *temp);
extern Temporal *teq_tbool_bool(const Temporal *temp, bool b);
extern Temporal *teq_temporal_temporal(const Temporal *temp1, const Temporal *temp2);
extern Temporal *teq_text_ttext(const text *txt, const Temporal *temp);
extern Temporal *teq_tfloat_float(const Temporal *temp, double d);
extern Temporal *teq_tpoint_point(const Temporal *temp, const GSERIALIZED *gs);
extern Temporal *teq_tint_int(const Temporal *temp, int i);
extern Temporal *teq_ttext_text(const Temporal *temp, const text *txt);
extern Temporal *tge_float_tfloat(double d, const Temporal *temp);
extern Temporal *tge_int_tint(int i, const Temporal *temp);
extern Temporal *tge_temporal_temporal(const Temporal *temp1, const Temporal *temp2);
extern Temporal *tge_text_ttext(const text *txt, const Temporal *temp);
extern Temporal *tge_tfloat_float(const Temporal *temp, double d);
extern Temporal *tge_tint_int(const Temporal *temp, int i);
extern Temporal *tge_ttext_text(const Temporal *temp, const text *txt);
extern Temporal *tgt_float_tfloat(double d, const Temporal *temp);
extern Temporal *tgt_int_tint(int i, const Temporal *temp);
extern Temporal *tgt_temporal_temporal(const Temporal *temp1, const Temporal *temp2);
extern Temporal *tgt_text_ttext(const text *txt, const Temporal *temp);
extern Temporal *tgt_tfloat_float(const Temporal *temp, double d);
extern Temporal *tgt_tint_int(const Temporal *temp, int i);
extern Temporal *tgt_ttext_text(const Temporal *temp, const text *txt);
extern Temporal *tle_float_tfloat(double d, const Temporal *temp);
extern Temporal *tle_int_tint(int i, const Temporal *temp);
extern Temporal *tle_temporal_temporal(const Temporal *temp1, const Temporal *temp2);
extern Temporal *tle_text_ttext(const text *txt, const Temporal *temp);
extern Temporal *tle_tfloat_float(const Temporal *temp, double d);
extern Temporal *tle_tint_int(const Temporal *temp, int i);
extern Temporal *tle_ttext_text(const Temporal *temp, const text *txt);
extern Temporal *tlt_float_tfloat(double d, const Temporal *temp);
extern Temporal *tlt_int_tint(int i, const Temporal *temp);
extern Temporal *tlt_temporal_temporal(const Temporal *temp1, const Temporal *temp2);
extern Temporal *tlt_text_ttext(const text *txt, const Temporal *temp);
extern Temporal *tlt_tfloat_float(const Temporal *temp, double d);
extern Temporal *tlt_tint_int(const Temporal *temp, int i);
extern Temporal *tlt_ttext_text(const Temporal *temp, const text *txt);
extern Temporal *tne_bool_tbool(bool b, const Temporal *temp);
extern Temporal *tne_float_tfloat(double d, const Temporal *temp);
extern Temporal *tne_int_tint(int i, const Temporal *temp);
extern Temporal *tne_point_tpoint(const GSERIALIZED *gs, const Temporal *temp);
extern Temporal *tne_tbool_bool(const Temporal *temp, bool b);
extern Temporal *tne_temporal_temporal(const Temporal *temp1, const Temporal *temp2);
extern Temporal *tne_text_ttext(const text *txt, const Temporal *temp);
extern Temporal *tne_tfloat_float(const Temporal *temp, double d);
extern Temporal *tne_tpoint_point(const Temporal *temp, const GSERIALIZED *gs);
extern Temporal *tne_tint_int(const Temporal *temp, int i);
extern Temporal *tne_ttext_text(const Temporal *temp, const text *txt);

extern Temporal *tand_bool_tbool(bool b, const Temporal *temp);
extern Temporal *tand_tbool_bool(const Temporal *temp, bool b);
extern Temporal *tand_tbool_tbool(const Temporal *temp1, const Temporal *temp2);
extern SpanSet *tbool_when_true(const Temporal *temp);
extern Temporal *tnot_tbool(const Temporal *temp);
extern Temporal *tor_bool_tbool(bool b, const Temporal *temp);
extern Temporal *tor_tbool_bool(const Temporal *temp, bool b);
extern Temporal *tor_tbool_tbool(const Temporal *temp1, const Temporal *temp2);

extern Temporal *add_float_tfloat(double d, const Temporal *tnumber);
extern Temporal *add_int_tint(int i, const Temporal *tnumber);
extern Temporal *add_tfloat_float(const Temporal *tnumber, double d);
extern Temporal *add_tint_int(const Temporal *tnumber, int i);
extern Temporal *add_tnumber_tnumber(const Temporal *tnumber1, const Temporal *tnumber2);
extern Temporal *div_float_tfloat(double d, const Temporal *tnumber);
extern Temporal *div_int_tint(int i, const Temporal *tnumber);
extern Temporal *div_tfloat_float(const Temporal *tnumber, double d);
extern Temporal *div_tint_int(const Temporal *tnumber, int i);
extern Temporal *div_tnumber_tnumber(const Temporal *tnumber1, const Temporal *tnumber2);
extern double float_degrees(double value, bool normalize);
extern Temporal *mult_float_tfloat(double d, const Temporal *tnumber);
extern Temporal *mult_int_tint(int i, const Temporal *tnumber);
extern Temporal *mult_tfloat_float(const Temporal *tnumber, double d);
extern Temporal *mult_tint_int(const Temporal *tnumber, int i);
extern Temporal *mult_tnumber_tnumber(const Temporal *tnumber1, const Temporal *tnumber2);
extern Temporal *sub_float_tfloat(double d, const Temporal *tnumber);
extern Temporal *sub_int_tint(int i, const Temporal *tnumber);
extern Temporal *sub_tfloat_float(const Temporal *tnumber, double d);
extern Temporal *sub_tint_int(const Temporal *tnumber, int i);
extern Temporal *sub_tnumber_tnumber(const Temporal *tnumber1, const Temporal *tnumber2);
extern Temporal *tfloat_round(const Temporal *temp, int maxdd);
extern Temporal **tfloatarr_round(const Temporal **temp, int count, int maxdd);
extern Temporal *tfloat_degrees(const Temporal *temp, bool normalize);
extern Temporal *tfloat_derivative(const Temporal *temp);
extern Temporal *tfloat_radians(const Temporal *temp);
extern Temporal *tnumber_abs(const Temporal *temp);
extern Temporal *tnumber_angular_difference(const Temporal *temp);
extern Temporal *tnumber_delta_value(const Temporal *temp);

extern Temporal *textcat_text_ttext(const text *txt, const Temporal *temp);
extern Temporal *textcat_ttext_text(const Temporal *temp, const text *txt);
extern Temporal *textcat_ttext_ttext(const Temporal *temp1, const Temporal *temp2);
extern Temporal *ttext_upper(const Temporal *temp);
extern Temporal *ttext_lower(const Temporal *temp);

extern Temporal *distance_tfloat_float(const Temporal *temp, double d);
extern Temporal *distance_tint_int(const Temporal *temp, int i);
extern Temporal *distance_tnumber_tnumber(const Temporal *temp1, const Temporal *temp2);
extern Temporal *distance_tpoint_point(const Temporal *temp, const GSERIALIZED *gs);
extern Temporal *distance_tpoint_tpoint(const Temporal *temp1, const Temporal *temp2);
extern double nad_stbox_geo(const STBox *box, const GSERIALIZED *gs);
extern double nad_stbox_stbox(const STBox *box1, const STBox *box2);
extern double nad_tbox_tbox(const TBox *box1, const TBox *box2);
extern double nad_tfloat_float(const Temporal *temp, double d);
extern double nad_tfloat_tfloat(const Temporal *temp1, const Temporal *temp2);
extern int nad_tint_int(const Temporal *temp, int i);
extern int nad_tint_tint(const Temporal *temp1, const Temporal *temp2);
extern double nad_tnumber_tbox(const Temporal *temp, const TBox *box);
extern double nad_tpoint_geo(const Temporal *temp, const GSERIALIZED *gs);
extern double nad_tpoint_stbox(const Temporal *temp, const STBox *box);
extern double nad_tpoint_tpoint(const Temporal *temp1, const Temporal *temp2);
extern TInstant *nai_tpoint_geo(const Temporal *temp, const GSERIALIZED *gs);
extern TInstant *nai_tpoint_tpoint(const Temporal *temp1, const Temporal *temp2);
extern bool shortestline_tpoint_geo(const Temporal *temp, const GSERIALIZED *gs, GSERIALIZED **result);
extern bool shortestline_tpoint_tpoint(const Temporal *temp1, const Temporal *temp2, GSERIALIZED **result);

extern bool bearing_point_point(const GSERIALIZED *gs1, const GSERIALIZED *gs2, double *result);
extern Temporal *bearing_tpoint_point(const Temporal *temp, const GSERIALIZED *gs, bool invert);
extern Temporal *bearing_tpoint_tpoint(const Temporal *temp1, const Temporal *temp2);
extern Temporal *tpoint_angular_difference(const Temporal *temp);
extern Temporal *tpoint_azimuth(const Temporal *temp);
extern GSERIALIZED *tpoint_convex_hull(const Temporal *temp);
extern Temporal *tpoint_cumulative_length(const Temporal *temp);
extern bool tpoint_direction(const Temporal *temp, double *result);
extern Temporal *tpoint_get_x(const Temporal *temp);
extern Temporal *tpoint_get_y(const Temporal *temp);
extern Temporal *tpoint_get_z(const Temporal *temp);
extern bool tpoint_is_simple(const Temporal *temp);
extern double tpoint_length(const Temporal *temp);
extern Temporal *tpoint_speed(const Temporal *temp);
extern int tpoint_srid(const Temporal *temp);
extern STBox *tpoint_stboxes(const Temporal *temp, int *count);
extern GSERIALIZED *tpoint_trajectory(const Temporal *temp);

extern STBox *geo_expand_space(const GSERIALIZED *gs, double d);
Temporal *geo_to_tpoint(const GSERIALIZED *gs);
extern Temporal *tgeogpoint_to_tgeompoint(const Temporal *temp);
extern Temporal *tgeompoint_to_tgeogpoint(const Temporal *temp);
bool tpoint_AsMVTGeom(const Temporal *temp, const STBox *bounds, int32_t extent, int32_t buffer, bool clip_geom, GSERIALIZED **gsarr, int64 **timesarr, int *count);
extern STBox *tpoint_expand_space(const Temporal *temp, double d);
extern Temporal **tpoint_make_simple(const Temporal *temp, int *count);
extern Temporal *tpoint_round(const Temporal *temp, int maxdd);
extern Temporal **tpointarr_round(const Temporal **temp, int count, int maxdd);
extern Temporal *tpoint_set_srid(const Temporal *temp, int32 srid);
bool tpoint_to_geo_meas(const Temporal *tpoint, const Temporal *measure, bool segmentize, GSERIALIZED **result);

extern int econtains_geo_tpoint(const GSERIALIZED *gs, const Temporal *temp);
extern int edisjoint_tpoint_geo(const Temporal *temp, const GSERIALIZED *gs);
extern int edisjoint_tpoint_tpoint(const Temporal *temp1, const Temporal *temp2);
extern int edwithin_tpoint_geo(const Temporal *temp, const GSERIALIZED *gs, double dist);
extern int edwithin_tpoint_tpoint(const Temporal *temp1, const Temporal *temp2, double dist);
extern int eintersects_tpoint_geo(const Temporal *temp, const GSERIALIZED *gs);
extern int eintersects_tpoint_tpoint(const Temporal *temp1, const Temporal *temp2);
extern int etouches_tpoint_geo(const Temporal *temp, const GSERIALIZED *gs);

extern Temporal *tcontains_geo_tpoint(const GSERIALIZED *gs, const Temporal *temp, bool restr, bool atvalue);
extern Temporal *tdisjoint_tpoint_geo(const Temporal *temp, const GSERIALIZED *gs, bool restr, bool atvalue);
extern Temporal *tdwithin_tpoint_geo(const Temporal *temp, const GSERIALIZED *gs, double dist, bool restr, bool atvalue);
extern Temporal *tdwithin_tpoint_tpoint(const Temporal *temp1, const Temporal *temp2, double dist, bool restr, bool atvalue);
extern Temporal *tintersects_tpoint_geo(const Temporal *temp, const GSERIALIZED *gs, bool restr, bool atvalue);
extern Temporal *ttouches_tpoint_geo(const Temporal *temp, const GSERIALIZED *gs, bool restr, bool atvalue);

extern SkipList *tbool_tand_transfn(SkipList *state, const Temporal *temp);
extern SkipList *tbool_tor_transfn(SkipList *state, const Temporal *temp);
extern Span *temporal_extent_transfn(Span *p, const Temporal *temp);
extern Temporal *temporal_tagg_finalfn(SkipList *state);
extern SkipList *temporal_tcount_transfn(SkipList *state, const Temporal *temp);
extern SkipList *tfloat_tmax_transfn(SkipList *state, const Temporal *temp);
extern SkipList *tfloat_tmin_transfn(SkipList *state, const Temporal *temp);
extern SkipList *tfloat_tsum_transfn(SkipList *state, const Temporal *temp);
extern SkipList *tint_tmax_transfn(SkipList *state, const Temporal *temp);
extern SkipList *tint_tmin_transfn(SkipList *state, const Temporal *temp);
extern SkipList *tint_tsum_transfn(SkipList *state, const Temporal *temp);
extern TBox *tnumber_extent_transfn(TBox *box, const Temporal *temp);
extern double tnumber_integral(const Temporal *temp);
extern Temporal *tnumber_tavg_finalfn(SkipList *state);
extern SkipList *tnumber_tavg_transfn(SkipList *state, const Temporal *temp);
extern double tnumber_twavg(const Temporal *temp);
extern STBox *tpoint_extent_transfn(STBox *box, const Temporal *temp);
extern Temporal *tpoint_tcentroid_finalfn(SkipList *state);
extern SkipList *tpoint_tcentroid_transfn(SkipList *state, Temporal *temp);
extern GSERIALIZED *tpoint_twcentroid(const Temporal *temp);
extern SkipList *ttext_tmax_transfn(SkipList *state, const Temporal *temp);
extern SkipList *ttext_tmin_transfn(SkipList *state, const Temporal *temp);

extern Temporal *temporal_simplify_min_dist(const Temporal *temp, double dist);
extern Temporal *temporal_simplify_min_tdelta(const Temporal *temp, const Interval *mint);
extern Temporal *temporal_simplify_dp(const Temporal *temp, double eps_dist, bool synchronized);
extern Temporal *temporal_simplify_max_dist(const Temporal *temp, double eps_dist, bool synchronized);

extern Temporal *temporal_tprecision(const Temporal *temp, const Interval *duration, TimestampTz origin);
extern Temporal *temporal_tsample(const Temporal *temp, const Interval *duration, TimestampTz origin);

extern double temporal_dyntimewarp_distance(const Temporal *temp1, const Temporal *temp2);
extern Match *temporal_dyntimewarp_path(const Temporal *temp1, const Temporal *temp2, int *count);
extern double temporal_frechet_distance(const Temporal *temp1, const Temporal *temp2);
extern Match *temporal_frechet_path(const Temporal *temp1, const Temporal *temp2, int *count);
extern double temporal_hausdorff_distance(const Temporal *temp1, const Temporal *temp2);

extern double float_bucket(double value, double size, double origin);
extern Span *floatspan_bucket_list(const Span *bounds, double size, double origin, int *count);
extern int int_bucket(int value, int size, int origin);
extern Span *intspan_bucket_list(const Span *bounds, int size, int origin, int *count);
extern Span *period_bucket_list(const Span *bounds, const Interval *duration, TimestampTz origin, int *count);
extern STBox *stbox_tile_list(const STBox *bounds, double xsize, double ysize, double zsize, const Interval *duration, GSERIALIZED *sorigin, TimestampTz torigin, int *count);
extern TBox *tintbox_tile_list(const TBox *box, int xsize, const Interval *duration, int xorigin, TimestampTz torigin, int *count);
extern TBox *tfloatbox_tile_list(const TBox *box, double xsize, const Interval *duration, double xorigin, TimestampTz torigin, int *count);
extern Temporal **temporal_time_split(Temporal *temp, Interval *duration, TimestampTz torigin, TimestampTz **time_buckets, int *count);
extern Temporal **tfloat_value_split(Temporal *temp, double size, double origin, double **value_buckets, int *count);
extern Temporal **tfloat_value_time_split(Temporal *temp, double size, Interval *duration, double vorigin, TimestampTz torigin, double **value_buckets, TimestampTz **time_buckets, int *count);
extern TimestampTz timestamptz_bucket(TimestampTz timestamp, const Interval *duration, TimestampTz origin);
extern Temporal **tint_value_split(Temporal *temp, int size, int origin, int **value_buckets, int *count);
extern Temporal **tint_value_time_split(Temporal *temp, int size, Interval *duration, int vorigin, TimestampTz torigin, int **value_buckets, TimestampTz **time_buckets, int *count);

//-------------------- meos_catalog.h --------------------


//#include <stdbool.h>

typedef signed short int16;

typedef enum
{
  T_UNKNOWN        = 0,   
  T_BOOL           = 1,   
  T_DOUBLE2        = 2,   
  T_DOUBLE3        = 3,   
  T_DOUBLE4        = 4,   
  T_FLOAT8         = 5,   
  T_FLOATSET       = 6,   
  T_FLOATSPAN      = 7,   
  T_FLOATSPANSET   = 8,   
  T_INT4           = 9,   
  T_INT4RANGE      = 10,  
  T_INT4MULTIRANGE = 11,  
  T_INTSET         = 12,  
  T_INTSPAN        = 13,  
  T_INTSPANSET     = 14,  
  T_INT8           = 15,  
  T_BIGINTSET      = 16,  
  T_BIGINTSPAN     = 17,  
  T_BIGINTSPANSET  = 18,  
  T_STBOX          = 19,  
  T_TBOOL          = 20,  
  T_TBOX           = 21,  
  T_TDOUBLE2       = 22,  
  T_TDOUBLE3       = 23,  
  T_TDOUBLE4       = 24,  
  T_TEXT           = 25,  
  T_TEXTSET        = 26,  
  T_TFLOAT         = 27,  
  T_TIMESTAMPTZ    = 28,  
  T_TINT           = 29,  
  T_TSTZMULTIRANGE = 30,  
  T_TSTZRANGE      = 31,  
  T_TSTZSET        = 32,  
  T_TSTZSPAN       = 33,  
  T_TSTZSPANSET    = 34,  
  T_TTEXT          = 35,  
  T_GEOMETRY       = 36,  
  T_GEOMSET        = 37,  
  T_GEOGRAPHY      = 38,  
  T_GEOGSET        = 39,  
  T_TGEOMPOINT     = 40,  
  T_TGEOGPOINT     = 41,  
  T_NPOINT         = 42,  
  T_NPOINTSET      = 43,  
  T_NSEGMENT       = 44,  
  T_TNPOINT        = 45,  
} meosType;

#define NO_MEOS_TYPES 46
typedef enum
{
  UNKNOWN_OP      = 0,
  EQ_OP           = 1,  
  NE_OP           = 2,  
  LT_OP           = 3,  
  LE_OP           = 4,  
  GT_OP           = 5,  
  GE_OP           = 6,  
  ADJACENT_OP     = 7,  
  UNION_OP        = 8,  
  MINUS_OP        = 9,  
  INTERSECT_OP    = 10, 
  OVERLAPS_OP     = 11, 
  CONTAINS_OP     = 12, 
  CONTAINED_OP    = 13, 
  SAME_OP         = 14, 
  LEFT_OP         = 15, 
  OVERLEFT_OP     = 16, 
  RIGHT_OP        = 17, 
  OVERRIGHT_OP    = 18, 
  BELOW_OP        = 19, 
  OVERBELOW_OP    = 20, 
  ABOVE_OP        = 21, 
  OVERABOVE_OP    = 22, 
  FRONT_OP        = 23, 
  OVERFRONT_OP    = 24, 
  BACK_OP         = 25, 
  OVERBACK_OP     = 26, 
  BEFORE_OP       = 27, 
  OVERBEFORE_OP   = 28, 
  AFTER_OP        = 29, 
  OVERAFTER_OP    = 30, 
  EVEREQ_OP       = 31, 
  EVERNE_OP       = 32, 
  EVERLT_OP       = 33, 
  EVERLE_OP       = 34, 
  EVERGT_OP       = 35, 
  EVERGE_OP       = 36, 
  ALWAYSEQ_OP     = 37, 
  ALWAYSNE_OP     = 38, 
  ALWAYSLT_OP     = 39, 
  ALWAYSLE_OP     = 40, 
  ALWAYSGT_OP     = 41, 
  ALWAYSGE_OP     = 42, 
} meosOper;

typedef struct
{
  meosType temptype;    
  meosType basetype;    
} temptype_catalog_struct;

typedef struct
{
  meosType settype;     
  meosType basetype;    
} settype_catalog_struct;

typedef struct
{
  meosType spantype;    
  meosType basetype;    
} spantype_catalog_struct;

typedef struct
{
  meosType spansettype;    
  meosType spantype;       
} spansettype_catalog_struct;

extern const char *meostype_name(meosType temptype);
extern meosType temptype_basetype(meosType temptype);
extern meosType settype_basetype(meosType settype);
extern meosType spantype_basetype(meosType spantype);
extern meosType spantype_spansettype(meosType spantype);
extern meosType spansettype_spantype(meosType spansettype);
extern meosType basetype_spantype(meosType basetype);
extern meosType basetype_settype(meosType basetype);

extern bool geo_basetype(meosType basetype);
extern bool spatial_basetype(meosType basetype);

extern bool time_type(meosType timetype);

extern bool set_type(meosType type);
extern bool numset_type(meosType type);
extern bool ensure_numset_type(meosType type);
extern bool timeset_type(meosType type);
extern bool set_spantype(meosType type);
extern bool ensure_set_spantype(meosType type);
extern bool alphanumset_type(meosType settype);
extern bool geoset_type(meosType type);
extern bool ensure_geoset_type(meosType type);
extern bool spatialset_type(meosType type);
extern bool ensure_spatialset_type(meosType type);

extern bool span_basetype(meosType type);
extern bool span_canon_basetype(meosType type);
extern bool span_type(meosType type);
extern bool numspan_basetype(meosType type);
extern bool numspan_type(meosType type);
extern bool ensure_numspan_type(meosType type);
extern bool timespan_basetype(meosType type);
extern bool timespan_type(meosType type);

extern bool spanset_type(meosType type);
extern bool timespanset_type(meosType type);

extern bool temporal_type(meosType temptype);
extern bool temptype_continuous(meosType temptype);
extern bool basetype_byvalue(meosType type);
extern bool basetype_varlength(meosType type);
extern int16 basetype_length(meosType basetype);
extern bool talpha_type(meosType temptype);
extern bool tnumber_type(meosType temptype);
extern bool ensure_tnumber_type(meosType temptype);
extern bool tnumber_basetype(meosType basetype);
extern bool tnumber_spantype(meosType settype);
extern bool tnumber_spansettype(meosType spansettype);
extern bool tspatial_type(meosType temptype);
extern bool ensure_tspatial_type(meosType temptype);
extern bool tspatial_basetype(meosType basetype);
extern bool tgeo_type(meosType type);
extern bool ensure_tgeo_type(meosType type);
extern bool ensure_tnumber_tgeo_type(meosType type);

 

//-------------------- meos_internal.h --------------------


//#include <json-c/json.h>

//#include <meos.h>
//#include "meos_catalog.h" 

#define ANYTEMPSUBTYPE  0
#define TINSTANT        1
#define TSEQUENCE       2
#define TSEQUENCESET    3

 

extern uint64 datum_hash(Datum d, meosType basetype);
extern uint64 datum_hash_extended(Datum d, meosType basetype, uint64 seed);

extern Set *set_in(const char *str, meosType basetype);
extern char *set_out(const Set *s, int maxdd);
extern Span *span_in(const char *str, meosType spantype);
extern char *span_out(const Span *s, int maxdd);
extern SpanSet *spanset_in(const char *str, meosType spantype);
extern char *spanset_out(const SpanSet *ss, int maxdd);

extern Set *set_compact(const Set *s);
extern Set *set_make(const Datum *values, int count, meosType basetype, bool ordered);
extern Set *set_make_exp(const Datum *values, int count, int maxcount, meosType basetype, bool ordered);
extern Set *set_make_free(Datum *values, int count, meosType basetype, bool ordered);
extern Span *span_make(Datum lower, Datum upper, bool lower_inc, bool upper_inc, meosType basetype);
extern void span_set(Datum lower, Datum upper, bool lower_inc, bool upper_inc, meosType basetype, Span *s);
extern SpanSet *spanset_compact(SpanSet *ss);
extern SpanSet *spanset_make_exp(Span *spans, int count, int maxcount, bool normalize, bool ordered);
extern SpanSet *spanset_make_free(Span *spans, int count, bool normalize);

extern Set *value_to_set(Datum d, meosType basetype);
extern Span *value_to_span(Datum d, meosType basetype);
extern SpanSet *value_to_spanset(Datum d, meosType basetype);

extern Datum set_end_value(const Set *s);
extern int set_mem_size(const Set *s);
extern void set_set_span(const Set *os, Span *s);
extern Datum set_start_value(const Set *s);
extern bool set_value_n(const Set *s, int n, Datum *result);
extern Datum *set_values(const Set *s);
extern int spanset_mem_size(const SpanSet *ss);
extern void spatialset_set_stbox(const Set *set, STBox *box);
extern void value_set_span(Datum d, meosType basetype, Span *s);

extern void floatspan_round_int(const Span *span, Datum size, Span *result);
extern void floatspan_set_intspan(const Span *s1, Span *s2);
extern void intspan_set_floatspan(const Span *s1, Span *s2);
extern Set *numset_shift_scale(const Set *s, Datum shift, Datum width, bool hasshift, bool haswidth);
extern Span *numspan_shift_scale(const Span *s, Datum shift, Datum width, bool hasshift, bool haswidth);
extern SpanSet *numspanset_shift_scale(const SpanSet *ss, Datum shift, Datum width, bool hasshift, bool haswidth);

extern void span_expand(const Span *s1, Span *s2);
extern void span_shift(Span *s, Datum value);
extern void spanset_shift(SpanSet *s, Datum value);

extern Span *spanbase_extent_transfn(Span *s, Datum d, meosType basetype);
extern Set *value_union_transfn(Set *state, Datum d, meosType basetype);

extern bool adjacent_span_value(const Span *s, Datum d, meosType basetype);
extern bool adjacent_spanset_value(const SpanSet *ss, Datum d, meosType basetype);
extern bool contains_span_value(const Span *s, Datum d, meosType basetype);
extern bool contains_spanset_value(const SpanSet *ss, Datum d, meosType basetype);
extern bool contains_set_value(const Set *s, Datum d, meosType basetype);
extern bool contained_value_span(Datum d, meosType basetype, const Span *s);
extern bool contained_value_set(Datum d, meosType basetype, const Set *s);
extern bool contained_value_spanset(Datum d, meosType basetype, const SpanSet *ss);

extern bool left_set_value(const Set *s, Datum d, meosType basetype);
extern bool left_span_value(const Span *s, Datum d, meosType basetype);
extern bool left_spanset_value(const SpanSet *ss, Datum d, meosType basetype);
extern bool left_value_set(Datum d, meosType basetype, const Set *s);
extern bool left_value_span(Datum d, meosType basetype, const Span *s);
extern bool left_value_spanset(Datum d, meosType basetype, const SpanSet *ss);
extern bool right_value_set(Datum d, meosType basetype, const Set *s);
extern bool right_set_value(const Set *s, Datum d, meosType basetype);
extern bool right_value_span(Datum d, meosType basetype, const Span *s);
extern bool right_value_spanset(Datum d, meosType basetype, const SpanSet *ss);
extern bool right_span_value(const Span *s, Datum d, meosType basetype);
extern bool right_spanset_value(const SpanSet *ss, Datum d, meosType basetype);
extern bool overleft_value_set(Datum d, meosType basetype, const Set *s);
extern bool overleft_set_value(const Set *s, Datum d, meosType basetype);
extern bool overleft_value_span(Datum d, meosType basetype, const Span *s);
extern bool overleft_value_spanset(Datum d, meosType basetype, const SpanSet *ss);
extern bool overleft_span_value(const Span *s, Datum d, meosType basetype);
extern bool overleft_spanset_value(const SpanSet *ss, Datum d, meosType basetype);
extern bool overright_value_set(Datum d, meosType basetype, const Set *s);
extern bool overright_set_value(const Set *s, Datum d, meosType basetype);
extern bool overright_value_span(Datum d, meosType basetype, const Span *s);
extern bool overright_value_spanset(Datum d, meosType basetype, const SpanSet *ss);
extern bool overright_span_value(const Span *s, Datum d, meosType basetype);
extern bool overright_spanset_value(const SpanSet *ss, Datum d, meosType basetype);

extern bool inter_span_span(const Span *s1, const Span *s2, Span *result);
extern bool intersection_set_value(const Set *s, Datum d, meosType basetype, Datum *result);
extern bool intersection_span_value(const Span *s, Datum d, meosType basetype, Datum *result);
extern bool intersection_spanset_value(const SpanSet *ss, Datum d, meosType basetype, Datum *result);
extern Set *minus_set_value(const Set *s, Datum d, meosType basetype);
extern SpanSet *minus_span_value(const Span *s, Datum d, meosType basetype);
extern SpanSet *minus_spanset_value(const SpanSet *ss, Datum d, meosType basetype);
extern bool minus_value_set(Datum d, meosType basetype, const Set *s, Datum *result);
extern bool minus_value_span(Datum d, meosType basetype, const Span *s, Datum *result);
extern bool minus_value_spanset(Datum d, meosType basetype, const SpanSet *ss, Datum *result);
extern Set *union_set_value(const Set *s, const Datum d, meosType basetype);
extern SpanSet *union_span_value(const Span *s, Datum v, meosType basetype);
extern SpanSet *union_spanset_value(const SpanSet *ss, Datum d, meosType basetype);

extern double distance_value_value(Datum l, Datum r, meosType basetype);
extern double distance_span_value(const Span *s, Datum d, meosType basetype);
extern double distance_spanset_value(const SpanSet *ss, Datum d, meosType basetype);
extern double distance_set_value(const Set *s, Datum d, meosType basetype);

extern TBox *number_period_to_tbox(Datum d, meosType basetype, const Span *p);
extern TBox *number_timestamp_to_tbox(Datum d, meosType basetype, TimestampTz t);
extern void stbox_set(bool hasx, bool hasz, bool geodetic, int32 srid, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, const Span *p, STBox *box);
extern void tbox_set(const Span *s, const Span *p, TBox *box);

extern void float_set_tbox(double d, TBox *box);
extern bool geo_set_stbox(const GSERIALIZED *gs, STBox *box);
extern void geoarr_set_stbox(const Datum *values, int count, STBox *box);
extern void int_set_tbox(int i, TBox *box);
extern void number_set_tbox(Datum d, meosType basetype, TBox *box);
extern void numset_set_tbox(const Set *s, TBox *box);
extern void numspan_set_tbox(const Span *span, TBox *box);
extern void numspanset_set_tbox(const SpanSet *ss, TBox *box);
extern void period_set_stbox(const Span *p, STBox *box);
extern void period_set_tbox(const Span *p, TBox *box);
extern void periodset_set_stbox(const SpanSet *ps, STBox *box);
extern void periodset_set_tbox(const SpanSet *ps, TBox *box);
extern void stbox_set_box3d(const STBox *box, BOX3D *box3d);
extern void stbox_set_gbox(const STBox *box, GBOX *gbox);
extern void timestamp_set_stbox(TimestampTz t, STBox *box);
extern void timestamp_set_tbox(TimestampTz t, TBox *box);
extern void timestampset_set_stbox(const Set *ts, STBox *box);
extern void timestampset_set_tbox(const Set *ts, TBox *box);

extern TBox *tbox_shift_scale_value(const TBox *box, Datum shift, Datum width, bool hasshift, bool haswidth);
extern void stbox_expand(const STBox *box1, STBox *box2);
extern void tbox_expand(const TBox *box1, TBox *box2);

extern void bbox_union_span_span(const Span *s1, const Span *s2, Span *result);
 

extern char **geoarr_as_text(const Datum *geoarr, int count, int maxdd, bool extended);
extern char *tboolinst_as_mfjson(const TInstant *inst, bool with_bbox);
extern TInstant *tboolinst_in(const char *str);
extern char *tboolseq_as_mfjson(const TSequence *seq, bool with_bbox);
extern TSequence *tboolseq_in(const char *str, interpType interp);
extern char *tboolseqset_as_mfjson(const TSequenceSet *ss, bool with_bbox);
extern TSequenceSet *tboolseqset_in(const char *str);
extern Temporal *temporal_in(const char *str, meosType temptype);
extern char *temporal_out(const Temporal *temp, int maxdd);
extern Datum *temporal_values(const Temporal *temp, int *count);
extern char **temporalarr_out(const Temporal **temparr, int count, int maxdd);
extern char *tfloatinst_as_mfjson(const TInstant *inst, bool with_bbox, int precision);
extern TInstant *tfloatinst_in(const char *str);
extern char *tfloatseq_as_mfjson(const TSequence *seq, bool with_bbox, int precision);
extern TSequence *tfloatseq_in(const char *str, interpType interp);
extern char *tfloatseqset_as_mfjson(const TSequenceSet *ss, bool with_bbox, int precision);
extern TSequenceSet *tfloatseqset_in(const char *str);
extern TInstant *tgeogpointinst_in(const char *str);
extern TSequence *tgeogpointseq_in(const char *str, interpType interp);
extern TSequenceSet *tgeogpointseqset_in(const char *str);
extern TInstant *tgeompointinst_in(const char *str);
extern TSequence *tgeompointseq_in(const char *str, interpType interp);
extern TSequenceSet *tgeompointseqset_in(const char *str);
extern char *tinstant_as_mfjson(const TInstant *inst, int precision, bool with_bbox, char *srs);
extern TInstant *tinstant_in(const char *str, meosType temptype);
extern char *tinstant_out(const TInstant *inst, int maxdd);
extern char *tintinst_as_mfjson(const TInstant *inst, bool with_bbox);
extern TInstant *tintinst_in(const char *str);
extern char *tintseq_as_mfjson(const TSequence *seq, bool with_bbox);
extern TSequence *tintseq_in(const char *str, interpType interp);
extern char *tintseqset_as_mfjson(const TSequenceSet *ss, bool with_bbox);
extern TSequenceSet *tintseqset_in(const char *str);
extern char **tpointarr_as_text(const Temporal **temparr, int count, int maxdd, bool extended);
extern char *tpointinst_as_mfjson(const TInstant *inst, bool with_bbox, int precision, char *srs);
extern char *tpointseq_as_mfjson(const TSequence *seq, bool with_bbox, int precision, char *srs);
extern char *tpointseqset_as_mfjson(const TSequenceSet *ss, bool with_bbox, int precision, char *srs);
extern char *tsequence_as_mfjson(const TSequence *seq, int precision, bool with_bbox, char *srs);
extern TSequence *tsequence_in(const char *str, meosType temptype, interpType interp);
extern char *tsequence_out(const TSequence *seq, int maxdd);
extern char *tsequenceset_as_mfjson(const TSequenceSet *ss, int precision, bool with_bbox, char *srs);
extern TSequenceSet *tsequenceset_in(const char *str, meosType temptype, interpType interp);
extern char *tsequenceset_out(const TSequenceSet *ss, int maxdd);
extern char *ttextinst_as_mfjson(const TInstant *inst, bool with_bbox);
extern TInstant *ttextinst_in(const char *str);
extern char *ttextseq_as_mfjson(const TSequence *seq, bool with_bbox);
extern TSequence *ttextseq_in(const char *str, interpType interp);
extern char *ttextseqset_as_mfjson(const TSequenceSet *ss, bool with_bbox);
extern TSequenceSet *ttextseqset_in(const char *str);

extern Temporal *temporal_from_base_temp(Datum value, meosType temptype, const Temporal *temp);
extern TInstant *tinstant_copy(const TInstant *inst);
extern TInstant *tinstant_make(Datum value, meosType temptype, TimestampTz t);
extern TSequence *tpointseq_make_coords(const double *xcoords, const double *ycoords, const double *zcoords, const TimestampTz *times, int count, int32 srid, bool geodetic, bool lower_inc, bool upper_inc, interpType interp, bool normalize);
extern TSequence *tsequence_from_base_timestampset(Datum value, meosType temptype, const Set *ss);
extern TSequence *tsequence_make_exp(const TInstant **instants, int count, int maxcount, bool lower_inc, bool upper_inc, interpType interp, bool normalize);
extern TSequence *tsequence_compact(const TSequence *seq);
extern void tsequence_restart(TSequence *seq, int last);
extern TSequence *tsequence_subseq(const TSequence *seq, int from, int to, bool lower_inc, bool upper_inc);
extern TSequence *tsequence_copy(const TSequence *seq);
extern TSequence *tsequence_from_base_period(Datum value, meosType temptype, const Span *p, interpType interp);
extern TSequence *tsequence_make_free(TInstant **instants, int count, bool lower_inc, bool upper_inc, interpType interp, bool normalize);
extern TSequenceSet *tsequenceset_make_exp(const TSequence **sequences, int count, int maxcount, bool normalize);
extern TSequenceSet *tsequenceset_compact(const TSequenceSet *ss);
extern TSequenceSet *tsequenceset_make_free(TSequence **sequences, int count, bool normalize);
extern void tsequenceset_restart(TSequenceSet *ss, int last);
extern TSequenceSet *tsequenceset_copy(const TSequenceSet *ss);
extern TSequenceSet *tseqsetarr_to_tseqset(TSequenceSet **seqsets, int count, int totalseqs);
extern TSequenceSet *tsequenceset_from_base_periodset(Datum value, meosType temptype, const SpanSet *ps, interpType interp);

extern void temporal_set_period(const Temporal *temp, Span *p);
extern void tinstant_set_period(const TInstant *inst, Span *p);
extern void tsequence_set_period(const TSequence *seq, Span *p);
extern void tsequenceset_set_period(const TSequenceSet *ss, Span *p);

extern Datum temporal_end_value(const Temporal *temp);
extern Datum temporal_max_value(const Temporal *temp);
extern size_t temporal_mem_size(const Temporal *temp);
extern Datum temporal_min_value(const Temporal *temp);
extern void temporal_set_bbox(const Temporal *temp, void *box);
extern TSequence *tfloatseq_derivative(const TSequence *seq);
extern TSequenceSet *tfloatseqset_derivative(const TSequenceSet *ss);
extern void tnumber_set_span(const Temporal *temp, Span *span);
extern Datum temporal_start_value(const Temporal *temp);
extern TInstant *tnumberinst_abs(const TInstant *inst);
extern TSequence *tnumberseq_abs(const TSequence *seq);
extern TSequenceSet *tnumberseqset_abs(const TSequenceSet *ss);
extern TSequence *tnumberseq_angular_difference(const TSequence *seq);
extern TSequence *tnumberseqset_angular_difference(const TSequenceSet *ss);
extern TSequence *tnumberseq_delta_value(const TSequence *seq);
extern TSequenceSet *tnumberseqset_delta_value(const TSequenceSet *ss);
extern SpanSet *tnumberinst_valuespans(const TInstant *inst);
extern SpanSet *tnumberseq_valuespans(const TSequence *seq);
extern SpanSet *tnumberseqset_valuespans(const TSequenceSet *ss);
extern uint64 tinstant_hash(const TInstant *inst);
extern const TInstant **tinstant_instants(const TInstant *inst, int *count);
extern void tinstant_set_bbox(const TInstant *inst, void *box);
extern SpanSet *tinstant_time(const TInstant *inst);
extern TimestampTz *tinstant_timestamps(const TInstant *inst, int *count);
extern Datum tinstant_value(const TInstant *inst);
extern bool tinstant_value_at_timestamp(const TInstant *inst, TimestampTz t, Datum *result);
extern Datum tinstant_value_copy(const TInstant *inst);
extern Datum *tinstant_values(const TInstant *inst, int *count);
extern Interval *tsequence_duration(const TSequence *seq);
extern TimestampTz tsequence_end_timestamp(const TSequence *seq);
extern uint64 tsequence_hash(const TSequence *seq);
extern const TInstant **tsequence_instants(const TSequence *seq);
extern const TInstant *tsequence_max_instant(const TSequence *seq);
extern Datum tsequence_max_value(const TSequence *seq);
extern const TInstant *tsequence_min_instant(const TSequence *seq);
extern Datum tsequence_min_value(const TSequence *seq);
extern TSequence **tsequence_segments(const TSequence *seq, int *count);
extern TSequence **tsequence_sequences(const TSequence *seq, int *count);
extern void tsequence_set_bbox(const TSequence *seq, void *box);
extern void tsequence_expand_bbox(TSequence *seq, const TInstant *inst);
extern void tsequenceset_expand_bbox(TSequenceSet *ss, const TSequence *seq);
extern TimestampTz tsequence_start_timestamp(const TSequence *seq);
extern SpanSet *tsequence_time(const TSequence *seq);
extern TimestampTz *tsequence_timestamps(const TSequence *seq, int *count);
extern bool tsequence_value_at_timestamp(const TSequence *seq, TimestampTz t, bool strict, Datum *result);
extern Datum *tsequence_values(const TSequence *seq, int *count);
extern Interval *tsequenceset_duration(const TSequenceSet *ss, bool boundspan);
extern TimestampTz tsequenceset_end_timestamp(const TSequenceSet *ss);
extern uint64 tsequenceset_hash(const TSequenceSet *ss);
extern const TInstant *tsequenceset_inst_n(const TSequenceSet *ss, int n);
extern const TInstant **tsequenceset_instants(const TSequenceSet *ss);
extern const TInstant *tsequenceset_max_instant(const TSequenceSet *ss);
extern Datum tsequenceset_max_value(const TSequenceSet *ss);
extern const TInstant *tsequenceset_min_instant(const TSequenceSet *ss);
extern Datum tsequenceset_min_value(const TSequenceSet *ss);
extern int tsequenceset_num_instants(const TSequenceSet *ss);
extern int tsequenceset_num_timestamps(const TSequenceSet *ss);
extern TSequence **tsequenceset_segments(const TSequenceSet *ss, int *count);
extern TSequence **tsequenceset_sequences(const TSequenceSet *ss);
extern const TSequence **tsequenceset_sequences_p(const TSequenceSet *ss);
extern void tsequenceset_set_bbox(const TSequenceSet *ss, void *box);
extern TimestampTz tsequenceset_start_timestamp(const TSequenceSet *ss);
extern SpanSet *tsequenceset_time(const TSequenceSet *ss);
extern bool tsequenceset_timestamp_n(const TSequenceSet *ss, int n, TimestampTz *result);
extern TimestampTz *tsequenceset_timestamps(const TSequenceSet *ss, int *count);
extern bool tsequenceset_value_at_timestamp(const TSequenceSet *ss, TimestampTz t, bool strict, Datum *result);
extern Datum *tsequenceset_values(const TSequenceSet *ss, int *count);

extern Temporal *tinstant_merge(const TInstant *inst1, const TInstant *inst2);
extern Temporal *tinstant_merge_array(const TInstant **instants, int count);
extern TInstant *tinstant_shift_time(const TInstant *inst, const Interval *interval);
extern TSequence *tinstant_to_tsequence(const TInstant *inst, interpType interp);
extern TSequenceSet *tinstant_to_tsequenceset(const TInstant *inst, interpType interp);
extern Temporal *tnumber_shift_scale_value(const Temporal *temp, Datum shift, Datum width, bool hasshift, bool haswidth);
extern TInstant *tnuminst_shift_value(const TInstant *inst, Datum shift);
extern TSequence *tnumberseq_shift_scale_value(const TSequence *seq, Datum shift, Datum width, bool hasshift, bool haswidth);
extern Temporal *tsequence_append_tinstant(TSequence *seq, const TInstant *inst, double maxdist, const Interval *maxt, bool expand);
extern Temporal *tsequence_append_tsequence(TSequence *seq1, const TSequence *seq2, bool expand);
extern Temporal *tsequence_merge(const TSequence *seq1, const TSequence *seq2);
extern Temporal *tsequence_merge_array(const TSequence **sequences, int count);
extern Temporal *tsequence_set_interp(const TSequence *seq, interpType interp);
extern TSequence *tsequence_shift_scale_time(const TSequence *seq, const Interval *shift, const Interval *duration);
extern TInstant *tsequence_to_tinstant(const TSequence *seq);
extern TSequenceSet *tsequence_to_tsequenceset(const TSequence *seq);
extern TSequenceSet *tsequence_to_tsequenceset_interp(const TSequence *seq, interpType interp);
extern TSequenceSet *tsequenceset_append_tinstant(TSequenceSet *ss, const TInstant *inst, double maxdist, const Interval *maxt, bool expand);
extern TSequenceSet *tsequenceset_append_tsequence(TSequenceSet *ss, const TSequence *seq, bool expand);
extern TSequenceSet *tsequenceset_merge(const TSequenceSet *ss1, const TSequenceSet *ss2);
extern TSequenceSet *tsequenceset_merge_array(const TSequenceSet **seqsets, int count);
extern Temporal *tsequenceset_set_interp(const TSequenceSet *ss, interpType interp);
extern TSequenceSet *tnumberseqset_shift_scale_value(const TSequenceSet *ss, Datum start, Datum width, bool hasshift, bool haswidth);
extern TSequenceSet *tsequenceset_shift_scale_time(const TSequenceSet *ss, const Interval *start, const Interval *duration);
extern TInstant *tsequenceset_to_tinstant(const TSequenceSet *ss);
extern TSequence *tsequenceset_to_discrete(const TSequenceSet *ss);
extern TSequenceSet *tsequenceset_to_step(const TSequenceSet *ss);
extern TSequenceSet *tsequenceset_to_linear(const TSequenceSet *ss);
extern TSequence *tsequenceset_to_tsequence(const TSequenceSet *ss);

extern bool temporal_bbox_restrict_set(const Temporal *temp, const Set *set);
extern Temporal *temporal_restrict_minmax(const Temporal *temp, bool min, bool atfunc);
extern Temporal *temporal_restrict_period(const Temporal *temp, const Span *p, bool atfunc);
extern Temporal *temporal_restrict_periodset(const Temporal *temp, const SpanSet *ps, bool atfunc);
extern Temporal *temporal_restrict_timestamp(const Temporal *temp, TimestampTz t, bool atfunc);
extern Temporal *temporal_restrict_timestampset(const Temporal *temp, const Set *ts, bool atfunc);
extern Temporal *temporal_restrict_value(const Temporal *temp, Datum value, bool atfunc);
extern Temporal *temporal_restrict_values(const Temporal *temp, const Set *set, bool atfunc);
extern bool temporal_value_at_timestamp(const Temporal *temp, TimestampTz t, bool strict, Datum *result);
extern TInstant *tinstant_restrict_period(const TInstant *inst, const Span *period, bool atfunc);
extern TInstant *tinstant_restrict_periodset(const TInstant *inst, const SpanSet *ps, bool atfunc);
extern TInstant *tinstant_restrict_timestamp(const TInstant *inst, TimestampTz t, bool atfunc);
extern TInstant *tinstant_restrict_timestampset(const TInstant *inst, const Set *ts, bool atfunc);
extern TInstant *tinstant_restrict_value(const TInstant *inst, Datum value, bool atfunc);
extern TInstant *tinstant_restrict_values(const TInstant *inst, const Set *set, bool atfunc);
extern Temporal *tnumber_restrict_span(const Temporal *temp, const Span *span, bool atfunc);
extern Temporal *tnumber_restrict_spanset(const Temporal *temp, const SpanSet *ss, bool atfunc);
extern TInstant *tnumberinst_restrict_span(const TInstant *inst, const Span *span, bool atfunc);
extern TInstant *tnumberinst_restrict_spanset(const TInstant *inst, const SpanSet *ss, bool atfunc);
extern TSequenceSet *tnumberseqset_restrict_span(const TSequenceSet *ss, const Span *span, bool atfunc);
extern TSequenceSet *tnumberseqset_restrict_spanset(const TSequenceSet *ss, const SpanSet *spanset, bool atfunc);
extern Temporal *tpoint_restrict_geom_time(const Temporal *temp, const GSERIALIZED *gs, const Span *zspan, const Span *period, bool atfunc);
extern Temporal *tpoint_restrict_stbox(const Temporal *temp, const STBox *box, bool border_inc, bool atfunc);
extern TInstant *tpointinst_restrict_geom_time(const TInstant *inst, const GSERIALIZED *gs, const Span *zspan, const Span *period, bool atfunc);
extern TInstant *tpointinst_restrict_stbox(const TInstant *inst, const STBox *box, bool border_inc, bool atfunc);
extern Temporal *tpointseq_restrict_geom_time(const TSequence *seq, const GSERIALIZED *gs, const Span *zspan, const Span *period, bool atfunc);
extern Temporal *tpointseq_restrict_stbox(const TSequence *seq, const STBox *box, bool border_inc, bool atfunc);
extern TSequenceSet *tpointseqset_restrict_geom_time(const TSequenceSet *ss, const GSERIALIZED *gs, const Span *zspan, const Span *period, bool atfunc);
extern TSequenceSet *tpointseqset_restrict_stbox(const TSequenceSet *ss, const STBox *box, bool border_inc, bool atfunc);
extern TSequence *tsequence_at_period(const TSequence *seq, const Span *p);
extern TInstant *tsequence_at_timestamp(const TSequence *seq, TimestampTz t);
extern Temporal *tsequence_restrict_period(const TSequence *seq, const Span *p, bool atfunc);
extern Temporal *tsequence_restrict_periodset(const TSequence *seq, const SpanSet *ps, bool atfunc);
extern TSequenceSet *tsequenceset_restrict_minmax(const TSequenceSet *ss, bool min, bool atfunc);
extern TSequenceSet *tsequenceset_restrict_period(const TSequenceSet *ss, const Span *p, bool atfunc);
extern TSequenceSet *tsequenceset_restrict_periodset(const TSequenceSet *ss, const SpanSet *ps, bool atfunc);
extern Temporal *tsequenceset_restrict_timestamp(const TSequenceSet *ss, TimestampTz t, bool atfunc);
extern Temporal *tsequenceset_restrict_timestampset(const TSequenceSet *ss, const Set *ts, bool atfunc);
extern TSequenceSet *tsequenceset_restrict_value(const TSequenceSet *ss, Datum value, bool atfunc);
extern TSequenceSet *tsequenceset_restrict_values(const TSequenceSet *ss, const Set *set, bool atfunc);

extern Temporal *distance_tnumber_number(const Temporal *temp, Datum value, meosType valuetype, meosType restype);
extern double nad_tnumber_number(const Temporal *temp, Datum value, meosType basetype);

extern bool temporal_always_eq(const Temporal *temp, Datum value);
extern bool temporal_always_le(const Temporal *temp, Datum value);
extern bool temporal_always_lt(const Temporal *temp, Datum value);
extern bool temporal_ever_eq(const Temporal *temp, Datum value);
extern bool temporal_ever_le(const Temporal *temp, Datum value);
extern bool temporal_ever_lt(const Temporal *temp, Datum value);
extern bool tinstant_always_eq(const TInstant *inst, Datum value);
extern bool tinstant_always_le(const TInstant *inst, Datum value);
extern bool tinstant_always_lt(const TInstant *inst, Datum value);
extern bool tinstant_ever_eq(const TInstant *inst, Datum value);
extern bool tinstant_ever_le(const TInstant *inst, Datum value);
extern bool tinstant_ever_lt(const TInstant *inst, Datum value);
extern bool tpointinst_always_eq(const TInstant *inst, Datum value);
extern bool tpointinst_ever_eq(const TInstant *inst, Datum value);
extern bool tpointseq_always_eq(const TSequence *seq, Datum value);
extern bool tpointseq_ever_eq(const TSequence *seq, Datum value);
extern bool tpointseqset_always_eq(const TSequenceSet *ss, Datum value);
extern bool tpointseqset_ever_eq(const TSequenceSet *ss, Datum value);
extern bool tsequence_always_eq(const TSequence *seq, Datum value);
extern bool tsequence_always_le(const TSequence *seq, Datum value);
extern bool tsequence_always_lt(const TSequence *seq, Datum value);
extern bool tsequence_ever_eq(const TSequence *seq, Datum value);
extern bool tsequence_ever_le(const TSequence *seq, Datum value);
extern bool tsequence_ever_lt(const TSequence *seq, Datum value);
extern bool tsequenceset_always_eq(const TSequenceSet *ss, Datum value);
extern bool tsequenceset_always_le(const TSequenceSet *ss, Datum value);
extern bool tsequenceset_always_lt(const TSequenceSet *ss, Datum value);
extern bool tsequenceset_ever_eq(const TSequenceSet *ss, Datum value);
extern bool tsequenceset_ever_le(const TSequenceSet *ss, Datum value);
extern bool tsequenceset_ever_lt(const TSequenceSet *ss, Datum value);

extern int tinstant_cmp(const TInstant *inst1, const TInstant *inst2);
extern bool tinstant_eq(const TInstant *inst1, const TInstant *inst2);
extern int tsequence_cmp(const TSequence *seq1, const TSequence *seq2);
extern bool tsequence_eq(const TSequence *seq1, const TSequence *seq2);
extern int tsequenceset_cmp(const TSequenceSet *ss1, const TSequenceSet *ss2);
extern bool tsequenceset_eq(const TSequenceSet *ss1, const TSequenceSet *ss2);

extern int tpointinst_srid(const TInstant *inst);
extern GSERIALIZED *tpointseq_trajectory(const TSequence *seq);
extern TSequenceSet *tpointseq_azimuth(const TSequence *seq);
extern TSequence *tpointseq_cumulative_length(const TSequence *seq, double prevlength);
extern bool tpointseq_is_simple(const TSequence *seq);
extern double tpointseq_length(const TSequence *seq);
extern TSequence *tpointseq_speed(const TSequence *seq);
extern int tpointseq_srid(const TSequence *seq);
extern STBox *tpointseq_stboxes(const TSequence *seq, int *count);
extern TSequenceSet *tpointseqset_azimuth(const TSequenceSet *ss);
extern TSequenceSet *tpointseqset_cumulative_length(const TSequenceSet *ss);
extern bool tpointseqset_is_simple(const TSequenceSet *ss);
extern double tpointseqset_length(const TSequenceSet *ss);
extern TSequenceSet *tpointseqset_speed(const TSequenceSet *ss);
extern int tpointseqset_srid(const TSequenceSet *ss);
extern STBox *tpointseqset_stboxes(const TSequenceSet *ss, int *count);
extern GSERIALIZED *tpointseqset_trajectory(const TSequenceSet *ss);
extern Temporal *tpoint_get_coord(const Temporal *temp, int coord);

extern TInstant *tgeompointinst_tgeogpointinst(const TInstant *inst, bool oper);
extern TSequence *tgeompointseq_tgeogpointseq(const TSequence *seq, bool oper);
extern TSequenceSet *tgeompointseqset_tgeogpointseqset(const TSequenceSet *ss, bool oper);
extern Temporal *tgeompoint_tgeogpoint(const Temporal *temp, bool oper);
extern TInstant *tpointinst_set_srid(const TInstant *inst, int32 srid);
extern TSequence **tpointseq_make_simple(const TSequence *seq, int *count);
extern TSequence *tpointseq_set_srid(const TSequence *seq, int32 srid);
extern TSequence **tpointseqset_make_simple(const TSequenceSet *ss, int *count);
extern TSequenceSet *tpointseqset_set_srid(const TSequenceSet *ss, int32 srid);

extern Temporal *tsequence_insert(const TSequence *seq1, const TSequence *seq2, bool connect);
extern TSequenceSet *tsequenceset_insert(const TSequenceSet *ss1, const TSequenceSet *ss2);
extern Temporal *tsequence_delete_timestamp(const TSequence *seq, TimestampTz t, bool connect);
extern Temporal *tsequence_delete_timestampset(const TSequence *seq, const Set *ts, bool connect);
extern Temporal *tsequence_delete_period(const TSequence *seq, const Span *p, bool connect);
extern Temporal *tsequence_delete_periodset(const TSequence *seq, const SpanSet *ps, bool connect);
extern TSequenceSet *tsequenceset_delete_timestamp(const TSequenceSet *ss, TimestampTz t);
extern TSequenceSet *tsequenceset_delete_timestampset(const TSequenceSet *ss, const Set *ts);
extern TSequenceSet *tsequenceset_delete_period(const TSequenceSet *ss, const Span *p);
extern TSequenceSet *tsequenceset_delete_periodset(const TSequenceSet *ss, const SpanSet *ps);

extern double tnumberseq_integral(const TSequence *seq);
extern double tnumberseq_twavg(const TSequence *seq);
extern double tnumberseqset_integral(const TSequenceSet *ss);
extern double tnumberseqset_twavg(const TSequenceSet *ss);
extern GSERIALIZED *tpointseq_twcentroid(const TSequence *seq);
extern GSERIALIZED *tpointseqset_twcentroid(const TSequenceSet *ss);

extern Temporal *temporal_compact(const Temporal *temp);

extern Temporal **tnumber_value_split(const Temporal *temp, Datum size,
  Datum origin, Datum **buckets, int *count);