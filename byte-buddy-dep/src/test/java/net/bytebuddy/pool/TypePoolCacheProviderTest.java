package net.bytebuddy.pool;

import net.bytebuddy.test.utility.MockitoRule;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class TypePoolCacheProviderTest {

    private static final String FOO = "foo";

    @Rule
    public TestRule mockitoRule = new MockitoRule(this);

    @Mock
    private TypePool.Resolution resolution;

    @Test
    public void testNoOp() throws Exception {
        assertThat(TypePool.CacheProvider.NoOp.INSTANCE.find(FOO), nullValue(TypePool.Resolution.class));
        assertThat(TypePool.CacheProvider.NoOp.INSTANCE.register(FOO, resolution), sameInstance(resolution));
        assertThat(TypePool.CacheProvider.NoOp.INSTANCE.find(FOO), nullValue(TypePool.Resolution.class));
        TypePool.CacheProvider.NoOp.INSTANCE.clear();
    }

    @Test
    public void testSimple() throws Exception {
        TypePool.CacheProvider simple = new TypePool.CacheProvider.Simple();
        assertThat(simple.find(FOO), nullValue(TypePool.Resolution.class));
        assertThat(simple.register(FOO, resolution), sameInstance(resolution));
        assertThat(simple.find(FOO), sameInstance(resolution));
        TypePool.Resolution resolution = mock(TypePool.Resolution.class);
        assertThat(simple.register(FOO, resolution), sameInstance(this.resolution));
        assertThat(simple.find(FOO), sameInstance(this.resolution));
        simple.clear();
        assertThat(simple.find(FOO), nullValue(TypePool.Resolution.class));
        assertThat(simple.register(FOO, resolution), sameInstance(resolution));
        assertThat(simple.find(FOO), sameInstance(resolution));
    }

    @Test
    public void testSimpleMap() {
        ConcurrentMap<String, TypePool.Resolution> storage = new ConcurrentHashMap<String, TypePool.Resolution>();
        TypePool.CacheProvider.Simple cacheProvider = new TypePool.CacheProvider.Simple(storage);
        assertThat(cacheProvider.getStorage(), sameInstance(storage));
    }
}
